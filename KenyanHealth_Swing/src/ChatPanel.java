import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatPanel extends JPanel {

    // --- COLORS ---
    private static final Color COL_BG = Color.decode("#F8F4F9");
    private static final Color COL_HEADER = Color.decode("#4A148C");
    private static final Color COL_ACCENT = Color.decode("#D81B60");

    private JTextPane chatArea;
    private JTextField inputField;
    private StyledDocument doc;

    // --- KNOWLEDGE BASE ---
    private Map<String, String[]> knowledgeBase;
    private Random random;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(COL_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize the Brain
        random = new Random();
        initKnowledgeBase();

        // 1. HEADER
        JLabel title = new JLabel("MindMate AI: Comprehensive Support");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COL_HEADER);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // 2. CHAT AREA
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(Color.WHITE);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        doc = chatArea.getStyledDocument();

        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        // 3. INPUT AREA
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(COL_BG);
        inputPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COL_HEADER, 1),
                new EmptyBorder(10, 10, 10, 10)));

        JButton sendBtn = new JButton("SEND");
        sendBtn.setBackground(COL_ACCENT);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendBtn.setFocusPainted(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.setPreferredSize(new Dimension(100, 45));

        ActionListener sendAction = e -> sendMessage();
        sendBtn.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        appendMessage("MindMate",
                "Hello. I am MindMate. I am trained to listen to almost anything—from stress and relationships to grief and burnout. What's on your mind?",
                false);
    }

    private void sendMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty())
            return;

        appendMessage("You", userText, true);
        inputField.setText("");

        // Simulate thinking delay
        Timer timer = new Timer(600, e -> {
            String reply = getBotResponse(userText.toLowerCase());
            appendMessage("MindMate", reply, false);
            ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    // =================================================================
    // THE BRAIN: EXPANDED KNOWLEDGE BASE
    // =================================================================

    private void initKnowledgeBase() {
        knowledgeBase = new HashMap<>();

        // --- 1. CRISIS & SAFETY (Top Priority) ---
        String[] crisisResponses = {
                "⚠️ I hear deep pain in your words. Please, your life matters. Call 1199 (Kenya Red Cross) or go to the nearest hospital.",
                "⚠️ You are not alone, even if it feels that way. Please reach out to Befrienders Kenya: +254 722 118 060.",
                "⚠️ Please stay with us. There is help available. Call a crisis line immediately: 988 or 1199."
        };
        addTopic(new String[] { "suicide", "kill myself", "die", "end it", "hurt myself", "no way out" },
                crisisResponses);

        // --- 2. ACADEMIC & SCHOOL STRESS ---
        String[] schoolResponses = {
                "Exams do not define your worth. Take it one topic at a time. Have you taken a break today?",
                "Academic pressure is real. Remember: 'Done is better than perfect.' What is the smallest task you can finish right now?",
                "It sounds like you're overwhelmed by school. Try the Pomodoro technique: Study 25 mins, rest 5 mins."
        };
        addTopic(new String[] { "exam", "school", "grades", "fail", "study", "homework", "class", "teacher",
                "university", "college" }, schoolResponses);

        // --- 3. WORKPLACE & CAREER ---
        String[] workResponses = {
                "Burnout is your body saying 'Enough'. Can you set a boundary today, like leaving work on time?",
                "Work stress follows us home if we let it. Try a 'transition ritual'—change your clothes or take a walk to signal the work day is done.",
                "Your job is what you do, not who you are. Don't let a bad day at work define your value."
        };
        addTopic(new String[] { "work", "job", "boss", "career", "fired", "hired", "interview", "office", "colleague",
                "burnout" }, workResponses);

        // --- 4. RELATIONSHIPS & LOVE ---
        String[] relationshipResponses = {
                "Relationships are complex. Communication is key. Have you told them exactly how their actions made you feel?",
                "You deserve to feel safe and respected in any relationship. Does this person make you feel drained or energized?",
                "Heartbreak is a physical pain. Be gentle with yourself. It's okay to grieve the future you thought you had."
        };
        addTopic(new String[] { "boyfriend", "girlfriend", "partner", "spouse", "husband", "wife", "breakup", "ex",
                "date", "love", "dumped" }, relationshipResponses);

        // --- 5. FAMILY & HOME ---
        String[] familyResponses = {
                "Family dynamics can be incredibly triggering. It is okay to set boundaries with family members to protect your peace.",
                "You cannot control how your family acts, only how you react. Take a deep breath before responding.",
                "Living in a tense home environment is exhausting. do you have a safe space (like a park or library) you can escape to?"
        };
        addTopic(new String[] { "mom", "dad", "mother", "father", "parent", "sister", "brother", "family", "home",
                "house" }, familyResponses);

        // --- 6. ANXIETY & PANIC ---
        String[] anxietyResponses = {
                "I know it feels scary, but this feeling will pass. Try 4-7-8 breathing: Inhale for 4, hold for 7, exhale for 8.",
                "Anxiety is a liar—it tells you the worst-case scenario is a fact. It's not. Look around you. What is real right now?",
                "Ground yourself: Find 5 blue things in the room. Name them out loud."
        };
        addTopic(new String[] { "anxious", "panic", "scared", "fear", "nervous", "shaking", "heart", "breath", "worry",
                "future" }, anxietyResponses);

        // --- 7. DEPRESSION & SADNESS ---
        String[] depressionResponses = {
                "Depression lies to you. It tells you that you are lazy or broken. You are neither. You are fighting a hard battle.",
                "On days when you can't run, walk. When you can't walk, crawl. Just don't stop.",
                "It is okay to not be okay. You don't have to 'fix' this feeling right this second. Just survive the day."
        };
        addTopic(new String[] { "sad", "depress", "cry", "tears", "hopeless", "empty", "numb", "dark", "bed", "tired" },
                depressionResponses);

        // --- 8. SLEEP & INSOMNIA ---
        String[] sleepResponses = {
                "A racing mind is the enemy of sleep. Try writing down your 'to-do' list for tomorrow so your brain can let go.",
                "Screen blue light stops melatonin. Can you try reading a physical book for 20 minutes?",
                "Rest is productive. You cannot pour from an empty cup."
        };
        addTopic(new String[] { "sleep", "awake", "insomnia", "nightmare", "dream", "tired", "exhausted", "energy" },
                sleepResponses);

        // --- 9. ANGER & FRUSTRATION ---
        String[] angerResponses = {
                "Anger is a secondary emotion—usually protecting us from hurt or fear. What is underneath the anger?",
                "It's okay to be angry, but it's not okay to be destructive. Try squeezing a pillow or going for a run.",
                "Take a step back. Respond, don't react."
        };
        addTopic(new String[] { "angry", "mad", "hate", "furious", "rage", "scream", "annoy", "frustrate" },
                angerResponses);

        // --- 10. SELF-IMAGE ---
        String[] selfResponses = {
                "You are your own harshest critic. Talk to yourself like you would talk to a friend.",
                "Your worth is not tied to your productivity, your looks, or your grades.",
                "Comparison is the thief of joy. Focus on your own journey."
        };
        addTopic(new String[] { "fat", "ugly", "stupid", "worthless", "failure", "body", "look", "weight" },
                selfResponses);
    }

    // Helper to map many keywords to one response list
    private void addTopic(String[] keywords, String[] responses) {
        for (String key : keywords) {
            knowledgeBase.put(key, responses);
        }
    }

    private String getBotResponse(String msg) {
        // 1. Check for specific keywords in our massive map
        for (String key : knowledgeBase.keySet()) {
            // We use spaces to ensure we match whole words (e.g., avoid matching "ass" in
            // "class")
            if (msg.contains(key)) {
                String[] replies = knowledgeBase.get(key);
                return replies[random.nextInt(replies.length)];
            }
        }

        // 2. Default "I'm listening" responses if no keyword matched
        String[] defaults = {
                "I'm listening. Tell me more about that.",
                "That sounds heavy. How long have you felt this way?",
                "I'm here with you. Go on.",
                "It takes courage to share that. How does that make you feel?",
                "I see. Is there anything specifically triggering this?",
                "You are safe here. Keep going."
        };
        return defaults[random.nextInt(defaults.length)];
    }

    // --- UI HELPERS (Same as before) ---
    private void appendMessage(String sender, String text, boolean isUser) {
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setBold(keyWord, true);
        if (isUser) {
            StyleConstants.setForeground(keyWord, COL_HEADER);
        } else {
            StyleConstants.setForeground(keyWord, Color.DARK_GRAY);
        }

        try {
            doc.insertString(doc.getLength(), sender + ": ", keyWord);
            SimpleAttributeSet textStyle = new SimpleAttributeSet();
            StyleConstants.setForeground(textStyle, Color.BLACK);
            doc.insertString(doc.getLength(), text + "\n\n", textStyle);
            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
