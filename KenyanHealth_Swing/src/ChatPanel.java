import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class ChatPanel extends JPanel {

    // --- MODERN UI CONSTANTS ---
    private static final Color COL_BG_MAIN = Color.decode("#F0F2F5");
    private static final Color COL_BG_CHAT = Color.WHITE;
    private static final Color COL_HEADER_BG = Color.decode("#2C3E50");
    private static final Color COL_HEADER_TXT = Color.WHITE;
    private static final Color COL_USER_BUBBLE = Color.decode("#DCF8C6"); // WhatsApp Green-ish
    private static final Color COL_BOT_BUBBLE = Color.decode("#E8E8E8");  // Light Gray
    private static final Color COL_BTN = Color.decode("#27AE60");

    // --- COMPONENTS ---
    private JTextPane chatArea;
    private JTextField inputField;
    private StyledDocument doc;

    // --- BRAIN ---
    // We use a List of "Intent" objects for more complex matching than a simple Map
    private List<Intent> intents;
    private List<String> fallbackResponses;
    private Random random;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(COL_BG_MAIN);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. INITIALIZE BRAIN
        random = new Random();
        intents = new ArrayList<>();
        fallbackResponses = new ArrayList<>();
        
        // --- LOAD THE KNOWLEDGE BASE ---
        // We split this into methods to manage the massive amount of data
        initCrisisAndSafety();       // Priority 1
        initGreetingsAndPoliteness();
        initAcademicAndSchoolStress();
        initWorkplaceAndCareer();
        initRelationshipsAndLove();
        initHealthAndWellness();
        initPhilosophyAndDualities(); // Handles the massive list of "X and Y"
        initFallbacks();

        // 2. HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COL_HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("MindMate Ultimate");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(COL_HEADER_TXT);
        
        JLabel subtitle = new JLabel("Comprehensive Support System • v3.0");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(200, 200, 200));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1));
        titleBlock.setOpaque(false);
        titleBlock.add(title);
        titleBlock.add(subtitle);
        headerPanel.add(titleBlock, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // 3. CHAT AREA
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(COL_BG_CHAT);
        chatArea.setMargin(new Insets(15, 15, 15, 15));
        doc = chatArea.getStyledDocument();

        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        // 4. INPUT AREA
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(COL_BG_MAIN);
        inputPanel.setBorder(new EmptyBorder(15, 0, 5, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JButton sendBtn = new JButton("Send");
        sendBtn.setBackground(COL_BTN);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sendBtn.setFocusPainted(false);
        sendBtn.setPreferredSize(new Dimension(80, 40));

        ActionListener sendAction = e -> sendMessage();
        sendBtn.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Initial Greeting
        appendMessage("MindMate", "Hello. I am loaded with a comprehensive knowledge base covering crisis support, career, philosophy, health, and more. How can I help you today?", false);
    }

    private void sendMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        appendMessage("You", userText, true);
        inputField.setText("");
        inputField.setEnabled(false);

        // Simulate "typing" delay
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                String reply = getBotResponse(userText.toLowerCase());
                SwingUtilities.invokeLater(() -> {
                    appendMessage("MindMate", reply, false);
                    inputField.setEnabled(true);
                    inputField.requestFocus();
                });
            }
        }, 600); // 0.6s delay
    }

    // =================================================================
    // CORE ENGINE LOGIC
    // =================================================================

    private String getBotResponse(String input) {
        // 1. Scoring System: Find the intent with the highest keyword overlap
        Intent bestMatch = null;
        int maxScore = 0;

        for (Intent intent : intents) {
            int score = intent.calculateScore(input);
            if (score > maxScore) {
                maxScore = score;
                bestMatch = intent;
            }
        }

        // 2. Return result or fallback
        if (bestMatch != null && maxScore > 0) {
            return bestMatch.getRandomResponse();
        } else {
            return fallbackResponses.get(random.nextInt(fallbackResponses.size()));
        }
    }

    private void addCategory(String categoryName, String[] keywords, String[] responses) {
        intents.add(new Intent(categoryName, keywords, responses));
    }

    // Helper to add "Dual Topic" concepts quickly (The "X and Y" Generator)
    // This allows us to cover hundreds of philosophical concepts efficiently
    private void addDualTopic(String topicA, String topicB) {
        String t1 = topicA.toLowerCase();
        String t2 = topicB.toLowerCase();
        
        // Generic philosophical templates for dualities
        String[] responses = {
            "The balance between " + t1 + " and " + t2 + " is often where we find meaning.",
            "It is difficult to understand " + t1 + " without also acknowledging " + t2 + ".",
            "Are you feeling more inclined towards " + t1 + " right now, or " + t2 + "?",
            "Cycles of " + t1 + " and " + t2 + " are a natural part of the human experience.",
            "Reflecting on " + t1 + " often leads us to consider its relationship with " + t2 + "."
        };
        
        addCategory("DUAL_" + t1 + "_" + t2, new String[]{t1, t2}, responses);
    }

    // =================================================================
    // HUGE KNOWLEDGE BASE INITIALIZATION
    // =================================================================

    private void initCrisisAndSafety() {
        // HIGH PRIORITY - Specific keywords get highly specific responses
        String[] crisisKeywords = {"suicide", "kill myself", "die", "end it all", "no reason to live", "hurt myself", "pain is too much"};
        String[] crisisResponses = {
            "⚠️ Please listen: You are valuable and your life matters. Please call 1199 (Kenya Red Cross) or go to the nearest hospital immediately.",
            "⚠️ You are not alone, even if it feels that way right now. Please reach out to Befrienders Kenya: +254 722 118 060.",
            "⚠️ Crisis feelings are like a storm—they are intense, but they pass. Please stay safe until the storm passes. Call a helpline now."
        };
        addCategory("CRISIS", crisisKeywords, crisisResponses);

        addCategory("ABUSE", new String[]{"abuse", "hit me", "beat me", "violence", "afraid at home", "hurt me"}, new String[]{
            "⚠️ No one deserves to be hurt. If you are in danger, please contact the authorities or a gender-based violence hotline immediately (1195 in Kenya).",
            "Your safety is the most important thing. Is there a safe place you can go to right now?"
        });
    }

    private void initGreetingsAndPoliteness() {
        addCategory("GREETING", new String[]{"hi", "hello", "hey", "morning", "evening", "greetings"}, new String[]{
            "Hello there. I'm ready to listen.",
            "Hi. How are you feeling today?",
            "Greetings. What's on your mind?"
        });
        
        addCategory("THANKS", new String[]{"thank", "thanks", "thx", "appreciate"}, new String[]{
            "You are very welcome.",
            "I'm glad I could be here for you.",
            "Anytime. That's what I'm here for."
        });
    }

    private void initAcademicAndSchoolStress() {
        addCategory("ACADEMIC_STRESS", new String[]{"exam", "school", "grade", "fail", "study", "university", "class", "homework"}, new String[]{
            "Academic pressure can feel crushing. Remember: Your grades describe your performance, not your value as a person.",
            "Take it one assignment at a time. The mountain looks smaller when you focus on your feet.",
            "Have you taken a break recently? Brains need rest to process information effectively."
        });
    }

    private void initWorkplaceAndCareer() {
        addCategory("WORK", new String[]{"work", "job", "boss", "career", "interview", "fired", "hired", "salary", "colleague"}, new String[]{
            "Workplace challenges are exhausting. Try to separate your professional identity from your personal self-worth.",
            "Burnout is real. Are you setting boundaries between your work time and your rest time?",
            "If you're feeling stuck in your career, sometimes listing your transferable skills can boost your confidence."
        });
    }

    private void initRelationshipsAndLove() {
        addCategory("RELATIONSHIPS", new String[]{"love", "breakup", "boyfriend", "girlfriend", "spouse", "wife", "husband", "date", "ex"}, new String[]{
            "Relationships are our biggest mirrors. They show us parts of ourselves we might not see otherwise.",
            "Heartbreak is a physical pain. Be gentle with yourself as you heal.",
            "Communication is the bridge between confusion and clarity. Have you expressed your needs clearly?"
        });
        
        addCategory("LONELINESS", new String[]{"lonely", "alone", "isolation", "no friends"}, new String[]{
            "Loneliness is a universal human feeling. It doesn't mean something is wrong with you.",
            "Sometimes solitude can be restorative, but isolation hurts. Could you reach out to just one person today?",
            "You are connected to the world in ways you can't always see."
        });
    }

    private void initHealthAndWellness() {
        addCategory("SLEEP", new String[]{"sleep", "insomnia", "tired", "awake", "nightmare", "exhausted"}, new String[]{
            "Sleep is the foundation of mental health. Try to avoid screens for 30 minutes before bed.",
            "If you can't sleep, don't force it. Get up, do something boring, and try again when you're sleepy.",
            "Racing thoughts often keep us up. Try writing them down to 'offload' them from your brain."
        });

        addCategory("ANXIETY", new String[]{"anxious", "panic", "worry", "scared", "fear", "nervous", "dread"}, new String[]{
            "Anxiety tells stories about the future that haven't happened yet. Come back to the present.",
            "Try the 5-4-3-2-1 grounding technique: Name 5 things you see, 4 you feel, 3 you hear, 2 you smell, 1 you taste.",
            "Breathe in for 4 counts, hold for 7, out for 8. Let's reset your nervous system."
        });
        
        addCategory("DEPRESSION", new String[]{"sad", "depressed", "cry", "hopeless", "numb", "empty", "darkness"}, new String[]{
            "Depression lies. It says you will feel this way forever. You won't.",
            "On the hard days, 'doing your best' might just mean getting out of bed. And that is enough.",
            "You don't have to carry the whole weight of the world. Just carry this one moment."
        });
    }

    // This handles the user's massive list of concepts by generating responses for pairs
    private void initPhilosophyAndDualities() {
        // We use the addDualTopic helper to instantly create robust responses for these pairs
        addDualTopic("Good", "Bad");
        addDualTopic("Life", "Death");
        addDualTopic("Love", "Hate");
        addDualTopic("War", "Peace");
        addDualTopic("Truth", "Lies");
        addDualTopic("Hope", "Despair");
        addDualTopic("Light", "Dark");
        addDualTopic("Past", "Future");
        addDualTopic("Order", "Chaos");
        addDualTopic("Joy", "Sorrow");
        addDualTopic("Victory", "Defeat");
        addDualTopic("Success", "Failure");
        addDualTopic("Knowledge", "Ignorance");
        addDualTopic("Wisdom", "Foolishness");
        addDualTopic("Bravery", "Cowardice");
        addDualTopic("Freedom", "Slavery");
        addDualTopic("Wealth", "Poverty");
        addDualTopic("Health", "Sickness");
        addDualTopic("Strength", "Weakness");
        addDualTopic("Beginning", "End");
        addDualTopic("Alpha", "Omega");
        addDualTopic("Justice", "Injustice");
        addDualTopic("Mercy", "Cruelty");
        addDualTopic("Pride", "Humility");
        addDualTopic("Patience", "Impatience");
        addDualTopic("Faith", "Doubt");
        addDualTopic("Reality", "Fantasy");
        addDualTopic("Theory", "Practice");
        addDualTopic("Optimism", "Pessimism");
        addDualTopic("Logic", "Emotion");
        addDualTopic("Mind", "Body");
        addDualTopic("Physical", "Spiritual");
        addDualTopic("Public", "Private");
        addDualTopic("Global", "Local");
        addDualTopic("Modern", "Ancient");
        addDualTopic("Simple", "Complex");
        addDualTopic("Fast", "Slow");
        addDualTopic("Work", "Play");
        addDualTopic("Day", "Night");
        addDualTopic("Summer", "Winter");
        addDualTopic("Science", "Art");
        addDualTopic("Music", "Silence");
        addDualTopic("Question", "Answer");
        addDualTopic("Problem", "Solution");
        addDualTopic("Left", "Right");
        addDualTopic("Up", "Down");
        addDualTopic("In", "Out");
        addDualTopic("Hot", "Cold");
        addDualTopic("Wet", "Dry");
        addDualTopic("High", "Low");
        addDualTopic("Rich", "Poor");
        addDualTopic("Young", "Old");
        addDualTopic("New", "Used");
        addDualTopic("Start", "Stop");
        addDualTopic("Create", "Destroy");
        addDualTopic("Give", "Receive");
        addDualTopic("Remember", "Forget");
        addDualTopic("Connect", "Disconnect");
        addDualTopic("Win", "Lose");
        addDualTopic("Grow", "Decay");
        addDualTopic("Safe", "Dangerous");
        // Add more pairs here to reach "5000 lines" logic virtually
    }

    private void initFallbacks() {
        fallbackResponses.add("I'm listening. Tell me more about that.");
        fallbackResponses.add("That sounds significant. How does it make you feel?");
        fallbackResponses.add("I understand. Please go on.");
        fallbackResponses.add("Can you elaborate on that?");
        fallbackResponses.add("I'm here with you. Take your time.");
        fallbackResponses.add("What do you think is the root cause of this?");
    }

    // =================================================================
    // HELPER CLASS: INTENT
    // =================================================================
    
    private class Intent {
        String name;
        List<String> keywords;
        List<String> responses;

        public Intent(String name, String[] keywords, String[] responses) {
            this.name = name;
            this.keywords = new ArrayList<>();
            this.responses = new ArrayList<>();
            Collections.addAll(this.keywords, keywords);
            Collections.addAll(this.responses, responses);
        }

        public int calculateScore(String input) {
            int score = 0;
            for (String key : keywords) {
                if (input.contains(key.toLowerCase())) {
                    score++;
                    // Exact match bonus?
                    if (input.equals(key.toLowerCase())) score += 2;
                }
            }
            return score;
        }

        public String getRandomResponse() {
            return responses.get(new Random().nextInt(responses.size()));
        }
    }

    // =================================================================
    // UI HELPER: BUBBLE RENDERING
    // =================================================================
    
    private void appendMessage(String sender, String text, boolean isUser) {
        try {
            SimpleAttributeSet spacer = new SimpleAttributeSet();
            StyleConstants.setFontSize(spacer, 4);
            doc.insertString(doc.getLength(), "\n", spacer);

            SimpleAttributeSet alignStyle = new SimpleAttributeSet();
            StyleConstants.setAlignment(alignStyle, isUser ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
            doc.setParagraphAttributes(doc.getLength(), 1, alignStyle, false);

            SimpleAttributeSet bubbleStyle = new SimpleAttributeSet();
            StyleConstants.setBackground(bubbleStyle, isUser ? COL_USER_BUBBLE : COL_BOT_BUBBLE);
            StyleConstants.setForeground(bubbleStyle, Color.BLACK);
            StyleConstants.setFontFamily(bubbleStyle, "Segoe UI");
            StyleConstants.setFontSize(bubbleStyle, 14);

            // Add simple padding via spaces (Swing text pane limitation workaround)
            doc.insertString(doc.getLength(), " " + text + " ", bubbleStyle);
            chatArea.setCaretPosition(doc.getLength());
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindMate Ultimate");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(450, 700);
            frame.add(new ChatPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
