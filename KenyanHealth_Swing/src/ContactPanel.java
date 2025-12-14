import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ContactPanel extends JPanel {

    // --- COLORS (Matching App.java) ---
    private static final Color COL_HEADER = Color.decode("#4A148C");
    private static final Color COL_ACCENT = Color.decode("#D81B60");
    private static final Color COL_BG_LIGHT = Color.decode("#F8F4F9");
    private static final Color COL_TEXT_DARK = Color.decode("#333333");

    public ContactPanel() {
        // 1. OUTER CONTAINER (The Background)
        setLayout(new GridBagLayout()); // This centers whatever is added to it
        setBackground(COL_BG_LIGHT); // Light background matching Dashboard

        // 2. INNER CARD (The White Box)
        JPanel card = new JPanel();
        card.setLayout(null); // Keep null layout for specific field positioning
        card.setPreferredSize(new Dimension(420, 520)); // Fixed size for the form
        card.setBackground(Color.WHITE);

        // Add a nice purple border and shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COL_HEADER, 1),
                new EmptyBorder(20, 20, 20, 20)));

        // --- CONTENT INSIDE THE CARD ---

        // Title
        JLabel title = new JLabel("CONTACT US");
        title.setForeground(COL_HEADER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(10, 20, 400, 40);
        card.add(title);

        // Name
        addLabel(card, "FULL NAME", 90);
        JTextField nameField = createField("Enter Your Full Name", 110);
        card.add(nameField);

        // Email
        addLabel(card, "EMAIL ADDRESS", 160);
        JTextField emailField = createField("Enter Your Email", 180);
        card.add(emailField);

        // Phone
        addLabel(card, "PHONE NUMBER", 230);
        JTextField phoneField = createField("Enter Your Phone Number", 250);
        // Number only validation
        phoneField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || phoneField.getText().length() >= 10) {
                    e.consume();
                }
            }
        });
        card.add(phoneField);

        // Address
        addLabel(card, "ADDRESS", 300);
        JTextField addrField = createField("Enter Your Address", 320);
        card.add(addrField);

        // Send Button
        JButton sendBtn = new JButton("SEND MESSAGE");
        sendBtn.setBackground(COL_ACCENT);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendBtn.setFocusPainted(false);
        sendBtn.setBorderPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.setBounds(110, 400, 200, 40);

        sendBtn.addActionListener(e -> {
            // Validation Logic
            String n = nameField.getText();
            String em = emailField.getText();
            String p = phoneField.getText();
            String a = addrField.getText();

            // Clear placeholders for checking
            if (n.contains("Enter"))
                n = "";
            if (em.contains("Enter"))
                em = "";
            if (p.contains("Enter"))
                p = "";
            if (a.contains("Enter"))
                a = "";

            if (n.isEmpty() || em.isEmpty() || p.isEmpty() || a.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Info",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!em.contains("@") || !em.contains(".")) {
                JOptionPane.showMessageDialog(this, "Invalid Email Address.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (p.length() != 10) {
                JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Message sent successfully!\nWe will contact you shortly.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                // Reset fields
                nameField.setText("Enter Your Full Name");
                nameField.setForeground(Color.GRAY);
                emailField.setText("Enter Your Email");
                emailField.setForeground(Color.GRAY);
                phoneField.setText("Enter Your Phone Number");
                phoneField.setForeground(Color.GRAY);
                addrField.setText("Enter Your Address");
                addrField.setForeground(Color.GRAY);
            }
        });
        card.add(sendBtn);

        // Add the card to the center of the screen
        add(card);
    }

    // --- HELPER METHODS ---

    private void addLabel(JPanel p, String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(COL_TEXT_DARK); // Dark text on white background
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setBounds(60, y, 200, 20);
        p.add(label);
    }

    private JTextField createField(String placeholder, int y) {
        JTextField field = new JTextField(placeholder);
        field.setBounds(60, y, 300, 35);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Placeholder Logic
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setBorder(BorderFactory.createLineBorder(COL_HEADER)); // Highlight on focus
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                }
            }
        });
        return field;
    }
}