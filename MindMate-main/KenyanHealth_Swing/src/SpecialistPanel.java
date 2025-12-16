import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;

public class SpecialistPanel extends JPanel {

    private static final Color COL_BG = Color.decode("#F8F4F9");
    private static final Color COL_HEADER = Color.decode("#4A148C");

    public SpecialistPanel() {
        setLayout(new BorderLayout());
        setBackground(COL_BG);

        // 1. Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COL_BG);
        header.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Specialist Directory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COL_HEADER);

        JLabel subtitle = new JLabel("Connect with certified mental health professionals near you.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // 2. Scrollable List of Specialists
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(COL_BG);
        listContainer.setBorder(new EmptyBorder(0, 40, 40, 40));

        // --- ADD SPECIALISTS HERE ---
        listContainer
                .add(createCard("Dr. Amani Kamau", "Clinical Psychologist", "Nairobi, Westlands", "+254 700 123 456"));
        listContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        listContainer.add(
                createCard("Dr. Ken Wanjala", "Psychiatrist (MD)", "Mombasa General Hospital", "+254 711 987 654"));
        listContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        listContainer.add(createCard("Ms. Sarah Ochieng", "Child & Adolescent Counselor", "Kisumu, Milimani",
                "+254 722 555 111"));
        listContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        listContainer.add(createCard("MindMate Crisis Team", "24/7 Emergency Support", "Remote / Nationwide",
                "1199 (Red Cross)"));

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createCard(String name, String role, String loc, String phone) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(1000, 110));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 20, 15, 20)));

        // Left: Info
        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setBackground(Color.WHITE);

        JLabel nLabel = new JLabel(name);
        nLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nLabel.setForeground(COL_HEADER);

        JLabel rLabel = new JLabel(role);
        rLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rLabel.setForeground(Color.decode("#D81B60")); // Pink accent

        JLabel lLabel = new JLabel("📍 " + loc);
        lLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lLabel.setForeground(Color.GRAY);

        info.add(nLabel);
        info.add(rLabel);
        info.add(lLabel);
        card.add(info, BorderLayout.CENTER);

        // Right: Buttons
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(Color.WHITE);

        JButton callBtn = new JButton("📞 " + phone);
        callBtn.setBackground(Color.decode("#E8F5E9")); // Light Green
        callBtn.setForeground(Color.decode("#2E7D32"));
        callBtn.setFocusPainted(false);
        callBtn.setBorderPainted(false);
        callBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btns.add(callBtn);
        card.add(btns, BorderLayout.EAST);

        return card;
    }
}