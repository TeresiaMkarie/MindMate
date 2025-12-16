import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatPanel extends JPanel {

    // --- MODERN COLOR PALETTE ---
    // A soft, calming palette suitable for mental health contexts
    private static final Color COL_BG_MAIN = Color.decode("#F5F7FB"); // Very light blue-grey
    private static final Color COL_BG_CHAT = Color.WHITE;
    private static final Color COL_HEADER_BG = Color.decode("#4A148C"); // Deep Purple
    private static final Color COL_HEADER_TXT = Color.WHITE;

    // Bubble Colors
    private static final Color COL_USER_BUBBLE = Color.decode("#E1BEE7"); // Light Purple for user
    private static final Color COL_BOT_BUBBLE = Color.decode("#E0F2F1"); // Light Teal for bot

    // Button
    private static final Color COL_BTN = Color.decode("#00897B"); // Professional Teal
    private static final Color COL_BTN_TXT = Color.WHITE;

    private JTextPane chatArea;
    private JTextField inputField;
    private StyledDocument doc;

    // --- KNOWLEDGE BASE ---
    private Map<String, String[]> knowledgeBase;
    private Random random;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(COL_BG_MAIN);
        // Add outer padding so the app doesn't touch the window edges
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize the Brain
        random = new Random();
        initKnowledgeBase();

        // =========================================
        // 1. HEADER (Professional App Bar Look)
        // =========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COL_HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20)); // Inner padding

        JLabel title = new JLabel("MindMate AI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(COL_HEADER_TXT);

        JLabel subtitle = new JLabel("Support Assistant • Online");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(230, 230, 230)); // Off-white

        JPanel titleBlock = new JPanel(new GridLayout(2, 1));
        titleBlock.setOpaque(false);
        titleBlock.add(title);
        titleBlock.add(subtitle);

        headerPanel.add(titleBlock, BorderLayout.WEST);

        // Add header to main layout
        add(headerPanel, BorderLayout.NORTH);

        // =========================================
        // 2. CHAT AREA (Clean & Spacious)
        // =========================================
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(COL_BG_CHAT);
        // Add internal padding to the text area
        chatArea.setMargin(new Insets(15, 15, 15, 15));
        doc = chatArea.getStyledDocument();

        JScrollPane scroll = new JScrollPane(chatArea);
        // Remove the default ugly 3D border, replace with a subtle flat line
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll, BorderLayout.CENTER);

        // =========================================
        // 3. INPUT AREA (Modern Capsule Look)
        // =========================================
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(COL_BG_MAIN);
        inputPanel.setBorder(new EmptyBorder(15, 0, 5, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Create a padded, flat border for the input
        inputField.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true), // Rounded-ish line
                new EmptyBorder(10, 10, 10, 10) // Internal padding
        ));

        JButton sendBtn = new JButton("Send");
        sendBtn.setBackground(COL_BTN);
        sendBtn.setForeground(COL_BTN_TXT);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sendBtn.setFocusPainted(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.setPreferredSize(new Dimension(80, 40));

        // Add action listeners
        ActionListener sendAction = e -> sendMessage();
        sendBtn.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Initial Greeting
        appendMessage("MindMate",
                "Hello. I am MindMate. I'm here to listen to anything—stress, relationships, grief, or burnout. You are in a safe space.",
                false);
    }

    private void sendMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty())
            return;

        appendMessage("You", userText, true);
        inputField.setText("");
        inputField.setEnabled(false); // Prevent spamming while thinking

        // Simulate thinking delay with a timer
        Timer timer = new Timer(800, e -> {
            String reply = getBotResponse(userText.toLowerCase());
            appendMessage("MindMate", reply, false);
            inputField.setEnabled(true);
            inputField.requestFocus();
            ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    // =================================================================
    // LOGIC: KNOWLEDGE BASE (Unchanged from your code)
    // =================================================================

    private void initKnowledgeBase() {
        knowledgeBase = new HashMap<>();

        // --- 1. CRISIS ---
        String[] crisisResponses = {
                "⚠️ I hear deep pain in your words. Please, your life matters. Call 1199 (Kenya Red Cross) or go to the nearest hospital.",
                "⚠️ You are not alone. Please reach out to Befrienders Kenya: +254 722 118 060.",
                "⚠️ Please stay with us. There is help available. Call a crisis line immediately: 988 or 1199."
        };
        addTopic(new String[] { "suicide", "kill myself", "die", "end it", "hurt myself", "no way out" },
                crisisResponses);

        // --- 2. SCHOOL ---
        String[] schoolResponses = {
                "Exams do not define your worth. Take it one topic at a time. Have you taken a break today?",
                "Remember: 'Done is better than perfect.' What is the smallest task you can finish right now?",
                "Try the Pomodoro technique: Study 25 mins, rest 5 mins."
        };
        addTopic(new String[] { "exam", "school", "grades", "fail", "study", "homework", "class", "university" },
                schoolResponses);

        // --- 3. WORK ---
        String[] workResponses = {
                "Burnout is your body saying 'Enough'. Can you set a boundary today?",
                "Try a 'transition ritual'—change your clothes or take a walk to signal the work day is done.",
                "Your job is what you do, not who you are."
        };
        addTopic(new String[] { "work", "job", "boss", "career", "fired", "office", "burnout" }, workResponses);

        // --- 4. RELATIONSHIPS ---
        String[] relationshipResponses = {
                "Relationships are complex. Have you told them exactly how their actions made you feel?",
                "You deserve to feel safe. Does this person make you feel drained or energized?",
                "It's okay to grieve the future you thought you had."
        };
        addTopic(new String[] { "boyfriend", "girlfriend", "partner", "husband", "wife", "breakup", "love" },
                relationshipResponses);

        // --- 5. ANXIETY ---
        String[] anxietyResponses = {
                "This feeling will pass. Try 4-7-8 breathing: Inhale 4, hold 7, exhale 8.",
                "Anxiety is a liar. Look around you. What is real right now?",
                "Ground yourself: Find 5 blue things in the room."
        };
        addTopic(new String[] { "anxious", "panic", "scared", "fear", "nervous", "worry" }, anxietyResponses);

        // --- 6. DEPRESSION ---
        String[] depressionResponses = {
                "Depression lies to you. You are fighting a hard battle.",
                "When you can't run, walk. When you can't walk, crawl. Just don't stop.",
                "It is okay to not be okay. Just survive the day."
        };
        addTopic(new String[] { "sad", "depress", "cry", "hopeless", "empty", "tired" }, depressionResponses);
    }

    private void addTopic(String[] keywords, String[] responses) {
        for (String key : keywords) {
            knowledgeBase.put(key, responses);
        }
    }

    private String getBotResponse(String msg) {
        for (String key : knowledgeBase.keySet()) {
            if (msg.contains(key)) {
                String[] replies = knowledgeBase.get(key);
                return replies[random.nextInt(replies.length)];
            }
        }
        String[] defaults = {
                "I'm listening. Tell me more about that.",
                "That sounds heavy. How long have you felt this way?",
                "I'm here with you. Go on.",
                "It takes courage to share that. How does that make you feel?",
                "You are safe here. Keep going."
        };
        return defaults[random.nextInt(defaults.length)];
    }

    // =================================================================
    // UI HELPERS: UPDATED FOR BUBBLE STYLE
    // =================================================================

    private void appendMessage(String sender, String text, boolean isUser) {
        try {
            // 1. Insert a spacer (blank line) before the message
            SimpleAttributeSet spacer = new SimpleAttributeSet();
            StyleConstants.setFontSize(spacer, 4);
            doc.insertString(doc.getLength(), "\n", spacer);

            // 2. Set Paragraph Alignment (Right for User, Left for Bot)
            SimpleAttributeSet paragraphStyle = new SimpleAttributeSet();
            if (isUser) {
                StyleConstants.setAlignment(paragraphStyle, StyleConstants.ALIGN_RIGHT);
            } else {
                StyleConstants.setAlignment(paragraphStyle, StyleConstants.ALIGN_LEFT);
            }
            doc.setParagraphAttributes(doc.getLength(), 1, paragraphStyle, false);

            // 3. Create the "Bubble" Styling
            SimpleAttributeSet bubbleStyle = new SimpleAttributeSet();
            StyleConstants.setFontFamily(bubbleStyle, "Segoe UI");
            StyleConstants.setFontSize(bubbleStyle, 14);

            if (isUser) {
                StyleConstants.setBackground(bubbleStyle, COL_USER_BUBBLE);
                StyleConstants.setForeground(bubbleStyle, Color.BLACK);
                StyleConstants.setBold(bubbleStyle, false);
            } else {
                StyleConstants.setBackground(bubbleStyle, COL_BOT_BUBBLE);
                StyleConstants.setForeground(bubbleStyle, Color.BLACK);
                StyleConstants.setItalic(bubbleStyle, false); // Bot text is standard
            }

            // 4. Insert the text with padding spaces to simulate a bubble shape
            // Note: JTextPane doesn't do real rounded bubbles easily, so we use
            // background color and spaces to create the visual block.
            doc.insertString(doc.getLength(), "  " + text + "  ", bubbleStyle);

            // Auto-scroll to bottom
            chatArea.setCaretPosition(doc.getLength());

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
