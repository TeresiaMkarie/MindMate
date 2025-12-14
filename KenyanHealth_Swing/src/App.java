import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*; // --- FIXED: Required for Area, Ellipse2D ---
import java.io.*;
import java.util.Scanner;

public class App {

    // --- COLORS (Purple Theme) ---
    private static final Color COL_BG_DARK = Color.decode("#2E1437");
    private static final Color COL_HEADER = Color.decode("#4A148C");
    private static final Color COL_ACCENT = Color.decode("#D81B60");
    private static final Color COL_BG_LIGHT = Color.decode("#F8F4F9");
    private static final Color COL_TEXT_LIGHT = new Color(225, 190, 231);

    // Chart Colors
    private static final Color COL_CHART_GAP = Color.decode("#E0E0E0");
    private static final Color COL_CHART_URBAN = Color.decode("#7B1FA2");
    private static final Color COL_CHART_RURAL = Color.decode("#FF9800");

    private JFrame frame;
    private JPanel mainContentArea;
    private JPanel sidebar;
    private String currentUser = null;

    // --- DATABASE FILE ---
    private static final String DB_FILE = "users.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().start());
    }

    public void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JFrame("MindMate - Mental Health Analytics");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1024, 768));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        initDatabase();
        showLoginScreen();

        frame.setVisible(true);
    }

    // =========================================================================
    // 1. DATABASE LOGIC (Text File)
    // =========================================================================
    private void initDatabase() {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean authenticate(String username, String password) {
        try (Scanner scanner = new Scanner(new File(DB_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    this.currentUser = username;
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String email, String password) {
        if (authenticate(username, password))
            return false;
        try (FileWriter fw = new FileWriter(DB_FILE, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + password + "," + email);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // 2. LOGIN SCREENS
    // =========================================================================
    private void showLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COL_BG_DARK);

        JPanel card = createAuthCard("Welcome to MindMate");
        JTextField userField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);

        addToCard(card, new JLabel("Username:"), userField);
        addToCard(card, new JLabel("Password:"), passField);

        JButton loginBtn = createStyledButton("LOGIN", COL_ACCENT, Color.WHITE);
        loginBtn.addActionListener(e -> {
            if (authenticate(userField.getText(), new String(passField.getPassword()))) {
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton signupBtn = createStyledButton("Register", COL_HEADER, Color.WHITE);
        signupBtn.addActionListener(e -> showSignupScreen());

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // --- FIXED: Uppercase ---
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(btnPanel, gbc);

        // Exit Button
        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn, Color.RED, Color.WHITE);
        exitBtn.addActionListener(e -> System.exit(0));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(exitBtn);
        card.add(bottomPanel, gbc);

        panel.add(card);
        frame.setContentPane(panel);
        frame.revalidate();
    }

    private void showSignupScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COL_BG_DARK);

        JPanel card = createAuthCard("Join MindMate");
        JTextField u = new JTextField(20);
        JTextField em = new JTextField(20);
        JPasswordField p = new JPasswordField(20);

        addToCard(card, new JLabel("User:"), u);
        addToCard(card, new JLabel("Email:"), em);
        addToCard(card, new JLabel("Pass:"), p);

        JButton regBtn = createStyledButton("REGISTER", COL_ACCENT, Color.WHITE);
        regBtn.addActionListener(e -> {
            if (registerUser(u.getText(), em.getText(), new String(p.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Success! Please Login.");
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Error registering.");
            }
        });

        JButton backBtn = createStyledButton("Back", COL_HEADER, Color.WHITE);
        backBtn.addActionListener(e -> showLoginScreen());

        JPanel bp = new JPanel();
        bp.setBackground(Color.WHITE);
        bp.add(regBtn);
        bp.add(backBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // --- FIXED ---
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(bp, gbc);

        panel.add(card);
        frame.setContentPane(panel);
        frame.revalidate();
    }

    // =========================================================================
    // 3. DASHBOARD & NAVIGATION
    // =========================================================================
    private void showDashboard() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COL_BG_LIGHT);

        root.add(createHeader(), BorderLayout.NORTH);

        sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        mainContentArea = new JPanel(new BorderLayout());
        mainContentArea.setBackground(COL_BG_LIGHT);
        root.add(mainContentArea, BorderLayout.CENTER);

        switchView("DASHBOARD");

        frame.setContentPane(root);
        frame.revalidate();
    }

    private void switchView(String viewName) {
        mainContentArea.removeAll();

        if (viewName.equals("DASHBOARD")) {
            mainContentArea.add(createDashboardContent());
        } else if (viewName.equals("CONTACTS")) {
            // MERGE LOGIC: Tries to load ContactPanel
            try {
                mainContentArea.add(new ContactPanel());
            } catch (Throwable e) {
                JLabel err = new JLabel("Contact Section Coming Soon", SwingConstants.CENTER);
                err.setFont(new Font("Segoe UI", Font.BOLD, 24));
                mainContentArea.add(err);
            }
        } else {
            JLabel temp = new JLabel("Section: " + viewName, SwingConstants.CENTER);
            temp.setFont(new Font("Segoe UI", Font.BOLD, 24));
            mainContentArea.add(temp);
        }

        mainContentArea.revalidate();
        mainContentArea.repaint();
    }

    private JPanel createSidebar() {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setBackground(COL_BG_DARK);
        bar.setPreferredSize(new Dimension(250, 0));
        bar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel l = new JLabel("NAVIGATION");
        l.setForeground(COL_ACCENT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bar.add(l);
        bar.add(Box.createRigidArea(new Dimension(0, 20)));

        addNavBtn(bar, "Strategic Overview", "DASHBOARD");
        addNavBtn(bar, "Specialist Directory", "CONTACTS");
        addNavBtn(bar, "Resource Mapping", "RESOURCES");
        bar.add(Box.createVerticalGlue());
        addNavBtn(bar, "Help & Support", "HELP");

        return bar;
    }

    private void addNavBtn(JPanel panel, String text, String viewKey) {
        JButton btn = createStyledButton(text, COL_BG_DARK, COL_TEXT_LIGHT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.addActionListener(e -> switchView(viewKey));
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    // --- DASHBOARD CONTENT (Charts + Mission) ---
    private JScrollPane createDashboardContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COL_BG_LIGHT);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title
        JLabel title = new JLabel("Kenya Mental Health Strategic Landscape");
        title.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 28));
        title.setForeground(COL_HEADER);
        content.add(title);

        JLabel subtitle = new JLabel("Data sourced from Ministry of Health Action Plan & National Guidance documents.");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitle.setForeground(Color.GRAY);
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // Charts Container
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        chartsPanel.setBackground(COL_BG_LIGHT);
        chartsPanel.setPreferredSize(new Dimension(1200, 400));
        chartsPanel.setMaximumSize(new Dimension(1600, 450));
        chartsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add Professional Custom Charts
        chartsPanel.add(new TreatmentGapChart());
        chartsPanel.add(new ResourceInequityChart());
        chartsPanel.add(new DiagnosticFunnelChart());

        content.add(chartsPanel);
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- MINDMATE MISSION (Added Back) ---
        JPanel missionPanel = new JPanel(new BorderLayout());
        missionPanel.setBackground(Color.WHITE);
        missionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, COL_ACCENT), // Pink accent line
                new EmptyBorder(20, 30, 20, 30)));
        missionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        missionPanel.setMaximumSize(new Dimension(1600, 150));

        JLabel missionTitle = new JLabel("The MindMate Mission");
        missionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        missionTitle.setForeground(COL_HEADER);
        missionPanel.add(missionTitle, BorderLayout.NORTH);

        JTextArea missionText = new JTextArea(
                "Data indicates a massive treatment gap (~75%) and severe inequity in specialist distribution towards urban tertiary facilities. "
                        +
                        "MindMate aims to bridge this divide by providing a decentralized, digital platform that connects individuals in primary "
                        +
                        "and community care settings directly with available specialists, maximizing the utilization of scarce resources.");
        missionText.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        missionText.setForeground(Color.DARK_GRAY);
        missionText.setLineWrap(true);
        missionText.setWrapStyleWord(true);
        missionText.setEditable(false);
        missionPanel.add(missionText, BorderLayout.CENTER);

        content.add(missionPanel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        return scroll;
    }

    // --- UI HELPERS ---
    private JPanel createHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(COL_HEADER);
        h.setBorder(new EmptyBorder(10, 20, 10, 20));
        h.setPreferredSize(new Dimension(1024, 60));

        JLabel t = new JLabel("MindMate Portal");
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JButton logout = createStyledButton("Logout", COL_ACCENT, Color.WHITE);
        logout.addActionListener(e -> {
            currentUser = null;
            showLoginScreen();
        });

        h.add(t, BorderLayout.WEST);
        h.add(logout, BorderLayout.EAST);
        return h;
    }

    private JButton createStyledButton(String t, Color bg, Color fg) {
        JButton b = new JButton(t);
        styleButton(b, bg, fg);
        return b;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createAuthCard(String t) {
        JPanel c = new JPanel(new GridBagLayout());
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COL_HEADER, 2),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(COL_HEADER);
        GridBagConstraints g = new GridBagConstraints();
        g.gridwidth = GridBagConstraints.REMAINDER; // --- FIXED ---
        g.insets = new Insets(0, 0, 20, 0);
        c.add(l, g);
        return c;
    }

    private void addToCard(JPanel c, JLabel l, JComponent f) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridwidth = GridBagConstraints.REMAINDER; // --- FIXED ---
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(5, 0, 5, 0);
        c.add(l, g);
        f.setPreferredSize(new Dimension(250, 35));
        c.add(f, g);
    }

    // --- CHARTS (Fixed Imports & Improved Designs) ---
    abstract static class BaseChartPanel extends JPanel {
        protected String title;

        public BaseChartPanel(String t) {
            this.title = t;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    new EmptyBorder(10, 10, 10, 10)));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(COL_HEADER);
            g.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g.drawString(title, 15, 25);
        }
    }

    // Chart 1: Donut Chart for Treatment Gap
    static class TreatmentGapChart extends BaseChartPanel {
        public TreatmentGapChart() {
            super("Treatment Gap (~75%)");
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Draw Donut
            int size = 180;
            int x = (getWidth() - size) / 2;
            int y = 50;

            // Unmet Need (Gray)
            g2.setColor(COL_CHART_GAP);
            g2.fillArc(x, y, size, size, 0, 360);

            // Received Care (Accent)
            g2.setColor(COL_ACCENT);
            g2.fillArc(x, y, size, size, 90, 90); // 25% slice

            // Inner Circle (White)
            g2.setColor(Color.WHITE);
            g2.fillOval(x + 40, y + 40, size - 80, size - 80);

            // Center Text
            g2.setColor(COL_HEADER);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            String centerText = "75%";
            int textX = x + (size - g2.getFontMetrics().stringWidth(centerText)) / 2;
            g2.drawString(centerText, textX, y + size / 2 + 8);

            // Legend
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.setColor(Color.GRAY);
            g2.drawString("Unmet Need", x + 55, y + size + 20);
        }
    }

    // Chart 2: Horizontal Bars for Resource Inequity
    static class ResourceInequityChart extends BaseChartPanel {
        public ResourceInequityChart() {
            super("Resource Distribution");
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth() - 60;
            int startX = 30;
            int h = 40;

            // Urban Bar
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("Urban / Tertiary Facilities", startX, 60);

            g2.setColor(COL_CHART_URBAN);
            g2.fillRoundRect(startX, 65, (int) (w * 0.85), h, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString("High Concentration", startX + 10, 90);

            // Rural Bar
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Rural / Primary Care", startX, 130);

            g2.setColor(COL_CHART_RURAL);
            g2.fillRoundRect(startX, 135, (int) (w * 0.15), h, 10, 10);
            g2.setColor(COL_CHART_RURAL);
            g2.drawString("Scarcity", startX + (int) (w * 0.15) + 10, 160);
        }
    }

    // Chart 3: Funnel for Diagnosis
    static class DiagnosticFunnelChart extends BaseChartPanel {
        public DiagnosticFunnelChart() {
            super("Diagnosis Funnel");
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth();
            int cx = w / 2;

            // Top Bar (Prevalence)
            int topW = 200;
            g2.setColor(COL_CHART_URBAN);
            g2.fillRoundRect(cx - topW / 2, 60, topW, 50, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("Prevalence", cx - 30, 90);

            // Bottom Bar (Diagnosis)
            int botW = 60;
            g2.setColor(COL_ACCENT);
            g2.fillRoundRect(cx - botW / 2, 120, botW, 50, 15, 15);
            g2.drawString("<10%", cx - 15, 150);

            // Connecting Lines
            g2.setColor(new Color(200, 200, 200, 100));
            Polygon poly = new Polygon();
            poly.addPoint(cx - topW / 2 + 10, 110);
            poly.addPoint(cx + topW / 2 - 10, 110);
            poly.addPoint(cx + botW / 2, 120);
            poly.addPoint(cx - botW / 2, 120);
            g2.fillPolygon(poly);

            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            g2.drawString("Huge missed opportunity", cx - 60, 190);
        }
    }
}