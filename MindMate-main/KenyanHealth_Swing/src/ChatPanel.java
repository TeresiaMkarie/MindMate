import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class ChatPanel extends JPanel {

    // --- COLORS ---
    private static final Color COL_BG = Color.decode("#F8F4F9");
    private static final Color COL_HEADER = Color.decode("#4A148C");
    private static final Color COL_ACCENT = Color.decode("#D81B60");

    private JTextPane chatArea;
    private JTextField inputField;
    private StyledDocument doc;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(COL_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. HEADER (Renamed)
        JLabel title = new JLabel("MindMate AI: Your Wellness Companion");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COL_HEADER);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // 2. CHAT DISPLAY AREA
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

        appendMessage("MindMate", "Hello. I am MindMate. I'm here to listen without judgment. What's on your mind?",
                false);
    }

    private void sendMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty())
            return;

        appendMessage("You", userText, true);
        inputField.setText("");

        Timer timer = new Timer(600, e -> {
            String reply = getBotResponse(userText.toLowerCase());
            appendMessage("MindMate", reply, false);
            ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    // --- EXPANDED BRAIN OF THE BOT ---
    private String getBotResponse(String msg) {

        // 1. CRISIS (Highest Priority)
        if (containsAny(msg,
                new String[] { "suicide", "kill myself", "die", "end it all", "hurt myself", "pain is too much" })) {
            return "⚠️ I hear that you are in deep pain, but you are not alone. Please reach out for help immediately:\n"
                    +
                    "• Kenya Red Cross: 1199\n" +
                    "• Befrienders Kenya: +254 722 118 060\n" +
                    "Your life has value. Please call them now.";
        }

        // 2. GREETINGS
        if (msg.matches(".*\\b(hi|hello|hey|greetings)\\b.*"))
            return "Hello! I'm here. How are you feeling right now?";
        if (msg.contains("bye"))
            return "Goodbye. Remember, self-care is a journey. I'm here whenever you need me.";
        if (msg.contains("thank"))
            return "You are very welcome. I'm glad I could help.";
        if (msg.contains("name") || msg.contains("who are you"))
            return "I am MindMate, your virtual mental health companion.";

        // 3. STRESS & ANXIETY
        if (containsAny(msg, new String[] { "stress", "pressure", "overwhelm", "too much", "busy" }))
            return "Stress is heavy. Try the '5-4-3-2-1' technique: Name 5 things you see, 4 you feel, 3 you hear, 2 you smell, and 1 you taste. It grounds you.";
        if (containsAny(msg, new String[] { "anxious", "panic", "scared", "fear", "nervous", "heart beating" }))
            return "Anxiety passes, even if it feels permanent. Take a slow breath... hold it... and release. Focus only on your breathing for a moment.";
        if (containsAny(msg, new String[] { "burnout", "exhausted", "tired", "drained", "can't focus" }))
            return "You sound burnt out. Remember: Rest is not a reward; it's a necessity. Can you take 10 minutes just to do nothing?";

        // 4. RELATIONSHIPS
        if (containsAny(msg, new String[] { "lonely", "alone", "no friends", "isolation" }))
            return "Loneliness is a universal human feeling, but it doesn't mean you are unlovable. Is there one small connection you can make today, even just a text?";
        if (containsAny(msg, new String[] { "breakup", "heartbreak", "dumped", "ex", "divorce" }))
            return "Heartbreak is physical pain. Be gentle with yourself. It's okay to grieve the relationship. Take it one hour at a time.";
        if (containsAny(msg, new String[] { "fight", "argue", "parents", "conflict", "shouting" }))
            return "Conflict is draining. If things are heated, it's okay to step away and say 'I need a moment to cool down' before continuing.";

        // 5. SELF-ESTEEM & MOOD
        if (containsAny(msg, new String[] { "ugly", "fat", "hate myself", "worthless", "stupid", "failure" }))
            return "I'm sorry you feel that way, but thoughts are not facts. You are worthy simply because you exist. What is one small thing you like about yourself?";
        if (containsAny(msg, new String[] { "sad", "cry", "tears", "down", "depressed", "blue" }))
            return "It's okay to let the tears flow. Sadness is an emotion that demands to be felt. I am sitting with you in this.";
        if (containsAny(msg, new String[] { "happy", "joy", "excited", "good news", "proud" }))
            return "That is wonderful! Holding onto these moments builds resilience. I'm proud of you too!";

        // 6. LIFE & PURPOSE
        if (containsAny(msg, new String[] { "purpose", "meaning", "lost", "don't know what to do" }))
            return "Feeling lost is often a sign of growth. You don't need to figure out your whole life today. Just figure out the next right step.";
        if (containsAny(msg, new String[] { "sleep", "insomnia", "awake" }))
            return "Sleep is the foundation of mental health. Try to dim the lights and avoid screens for 30 minutes. Your mind needs a signal to rest.";

        // DEFAULT
        return "I'm listening. Tell me more about that. How does it affect your day-to-day life?";
    }

    private boolean containsAny(String input, String[] keywords) {
        for (String word : keywords) {
            if (input.contains(word))
                return true;
        }
        return false;
    }

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