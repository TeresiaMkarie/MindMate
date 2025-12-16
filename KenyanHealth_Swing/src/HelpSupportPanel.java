import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class HelpSupportPanel extends JPanel {

    private static final Color COL_BG = Color.decode("#F8F4F9");
    private static final Color COL_HEADER = Color.decode("#4A148C");
    private static final Color COL_ACCENT = Color.decode("#D81B60");

    public HelpSupportPanel() {
        setLayout(new BorderLayout());
        setBackground(COL_BG);

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(COL_BG);
        header.setBorder(new EmptyBorder(30, 40, 20, 40));
        JLabel t = new JLabel("Help & Support");
        t.setFont(new Font("Segoe UI", Font.BOLD, 28));
        t.setForeground(COL_HEADER);
        JLabel s = new JLabel("Frequently asked questions and developer contact.");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        s.setForeground(Color.GRAY);
        header.add(t);
        header.add(s);
        add(header, BorderLayout.NORTH);

        // Split View: FAQs (Top) and Contact Form (Bottom)
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COL_BG);
        content.setBorder(new EmptyBorder(0, 40, 40, 40));

        // 1. FAQs
        addFAQ(content, "How do I find a doctor?",
                "Go to the 'Specialist Directory' tab to see a list of verified professionals.");
        addFAQ(content, "Is my data private?",
                "Yes. MindMate stores your journal and chat data locally on your device.");

        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // 2. Contact Form (Your Old Code, Refined)
        JLabel formTitle = new JLabel("Contact the Developers");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(COL_HEADER);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(formTitle);
        content.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        formCard.setMaximumSize(new Dimension(1000, 350));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        addFormField(formCard, "Your Name:", 0);
        addFormField(formCard, "Email:", 1);
        addFormField(formCard, "Message:", 2);

        // Send Button
        JButton send = new JButton("Send Feedback");
        send.setBackground(COL_ACCENT);
        send.setForeground(Color.WHITE);
        send.setFocusPainted(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 10, 20);

        send.addActionListener(e -> JOptionPane.showMessageDialog(this, "Feedback sent! We will review it shortly."));
        formCard.add(send, gbc);

        content.add(formCard);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void addFAQ(JPanel p, String q, String a) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(new EmptyBorder(10, 15, 10, 15));
        box.setMaximumSize(new Dimension(1000, 70));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel qL = new JLabel("Q: " + q);
        qL.setFont(new Font("Segoe UI", Font.BOLD, 14));
        qL.setForeground(COL_HEADER);
        JLabel aL = new JLabel(a);
        aL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aL.setForeground(Color.DARK_GRAY);

        box.add(qL, BorderLayout.NORTH);
        box.add(aL, BorderLayout.CENTER);
        p.add(box);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void addFormField(JPanel p, String label, int row) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 5, 20);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        g.gridy = row;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(l, g);

        g.gridx = 1;
        g.weightx = 1.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        JTextField t = new JTextField(20);
        p.add(t, g);
    }
}