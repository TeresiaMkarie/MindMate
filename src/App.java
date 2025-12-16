import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*; // This fixes the "Area" and "Ellipse2D" errors
import java.io.*;
import java.util.Scanner;

public class App {

    // --- COLORS ---
    private static final Color COL_BG_DARK = Color.decode("#2E1437");
    private static final Color COL_HEADER = Color.decode("#4A148C");
    private static final Color COL_ACCENT = Color.decode("#D81B60");
    private static final Color COL_BG_LIGHT = Color.decode("#F8F4F9");
    private static final Color COL_TEXT_DARK = Color.decode("#333333");
    private static final Color COL_TEXT_LIGHT = new Color(225, 190, 231);

    // Chart specific colors
    private static final Color COL_CHART_GAP = Color.decode("#E0E0E0");
    private static final Color COL_CHART_URBAN = Color.decode("#7B1FA2");
    private static final Color COL_CHART_RURAL = Color.decode("#ddcdb6ff");

    private JFrame frame;
    private String currentUser = null;
    private JPanel sidebar;
    private boolean isSidebarVisible = true;

    // Database File
    private static final String DB_FILE = "users.txt";

    public void start() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        frame = new JFrame("MindMate - Mental Health Analytics");
        frame.setUndecorated(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1024, 768));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        initDatabase();
        showLoginScreen();

        frame.setVisible(true);
    }

    // =========================================================================
    // 1. DATABASE LOGIC (Text File Version)
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
    // 2. AUTHENTICATION SCREENS
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
            if (userField.getText().isEmpty() || passField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(frame, "Please enter credentials.");
                return;
            }
            if (authenticate(userField.getText(), new String(passField.getPassword()))) {
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials. Try registering first.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton signupBtn = createStyledButton("Create Account", COL_HEADER, Color.WHITE);
        signupBtn.addActionListener(e -> showSignupScreen());

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);

        // FIX: REMAINDER must be uppercase
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
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

        JTextField userField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);

        addToCard(card, new JLabel("Username:"), userField);
        addToCard(card, new JLabel("Email:"), emailField);
        addToCard(card, new JLabel("Password:"), passField);

        JButton regBtn = createStyledButton("REGISTER", COL_ACCENT, Color.WHITE);
        regBtn.addActionListener(e -> {
            if (userField.getText().isEmpty() || emailField.getText().isEmpty()
                    || passField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(frame, "All fields required.");
                return;
            }
            if (registerUser(userField.getText(), emailField.getText(), new String(passField.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Account Created! Please Login.");
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "User already exists.");
            }
        });

        JButton backBtn = createStyledButton("Back to Login", COL_HEADER, Color.WHITE);
        backBtn.addActionListener(e -> showLoginScreen());

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(regBtn);
        btnPanel.add(backBtn);
        addPanelToCard(card, btnPanel);

        panel.add(card);
        frame.setContentPane(panel);
        frame.revalidate();
    }

    private JPanel createAuthCard(String titleText) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COL_HEADER, 2), BorderFactory.createEmptyBorder(30, 40, 30, 40)));
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COL_HEADER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(title, gbc);
        return card;
    }

    private void addToCard(JPanel card, JLabel lbl, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(lbl, gbc);
        field.setPreferredSize(new Dimension(250, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(field, gbc);
    }

    private void addPanelToCard(JPanel card, JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 0, 0, 0);
        card.add(panel, gbc);
    }

    // =========================================================================
    // 3. DASHBOARD
    // =========================================================================

    private void showDashboard() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COL_BG_LIGHT);
        root.add(createDashboardHeader(), BorderLayout.NORTH);
        sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);
        root.add(createDashboardContent(), BorderLayout.CENTER);
        frame.setContentPane(root);
        frame.revalidate();
    }

    private JPanel createDashboardHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COL_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.setPreferredSize(new Dimension(1024, 60));
        JPanel leftBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftBox.setOpaque(false);
        JButton menuBtn = createStyledButton("☰", COL_HEADER, Color.WHITE);
        menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        menuBtn.setMargin(new Insets(0, 0, 0, 0));
        menuBtn.addActionListener(e -> {
            isSidebarVisible = !isSidebarVisible;
            sidebar.setVisible(isSidebarVisible);
            frame.revalidate();
        });
        leftBox.add(menuBtn);
        JLabel title = new JLabel("MindMate Analytics Portal");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        leftBox.add(title);
        header.add(leftBox, BorderLayout.WEST);
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightBox.setOpaque(false);
        JLabel userLabel = new JLabel("User: " + (currentUser != null ? currentUser : "Guest"));
        userLabel.setForeground(COL_TEXT_LIGHT);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightBox.add(userLabel);
        JButton logoutBtn = createStyledButton("Logout", COL_ACCENT, Color.WHITE);
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            showLoginScreen();
        });
        rightBox.add(logoutBtn);
        header.add(rightBox, BorderLayout.EAST);
        return header;
    }

    private JPanel createSidebar() {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setBackground(COL_BG_DARK);
        bar.setPreferredSize(new Dimension(250, 0));
        bar.setBorder(new EmptyBorder(30, 15, 30, 15));
        JLabel label = new JLabel("MAIN NAVIGATION");
        label.setForeground(COL_ACCENT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.add(label);
        bar.add(Box.createRigidArea(new Dimension(0, 20)));
        addNavBtn(bar, "Strategic Overview", true);
        addNavBtn(bar, "Specialist Directory", false);
        addNavBtn(bar, "Resource Mapping", false);
        bar.add(Box.createVerticalGlue());
        addNavBtn(bar, "Help & Support", false);
        return bar;
    }

    private void addNavBtn(JPanel panel, String text, boolean isActive) {
        JButton btn = createStyledButton(text, isActive ? COL_ACCENT : COL_BG_DARK,
                isActive ? Color.WHITE : COL_TEXT_LIGHT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JScrollPane createDashboardContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COL_BG_LIGHT);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));
        JLabel title = new JLabel("Kenya Mental Health Strategic Landscape");
        title.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 28));
        title.setForeground(COL_HEADER);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        JLabel subtitle = new JLabel("Data sourced from Ministry of Health Action Plan & National Guidance documents.");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // Impressive Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        chartsPanel.setBackground(COL_BG_LIGHT);
        chartsPanel.setPreferredSize(new Dimension(1200, 400));
        chartsPanel.setMaximumSize(new Dimension(1600, 450));
        chartsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chartsPanel.add(new FundingGapChart());
        chartsPanel.add(new BurdenChart());
        chartsPanel.add(new TreatmentGapChart());
        content.add(chartsPanel);
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // Mission
        JPanel missionPanel = new JPanel(new BorderLayout());
        missionPanel.setBackground(Color.WHITE);
        missionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, COL_ACCENT), new EmptyBorder(20, 30, 20, 30)));
        missionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        missionPanel.setMaximumSize(new Dimension(1600, 150));
        JLabel missionTitle = new JLabel("The MindMate Intervention");
        missionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        missionTitle.setForeground(COL_HEADER);
        missionPanel.add(missionTitle, BorderLayout.NORTH);
        JTextArea missionText = new JTextArea(
                "Data indicates a massive treatment gap and severe inequity in specialist distribution. MindMate aims to bridge this divide by providing a digital platform that connects primary care settings directly with available specialists.");
        missionText.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        missionText.setForeground(COL_TEXT_DARK);
        missionText.setLineWrap(true);
        missionText.setWrapStyleWord(true);
        missionText.setEditable(false);
        missionPanel.add(missionText, BorderLayout.CENTER);
        content.add(missionPanel);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        return scroll;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- CHARTS ---
    abstract static class BaseChartPanel extends JPanel {
        protected String title;

        public BaseChartPanel(String title) {
            this.title = title;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    new EmptyBorder(20, 20, 20, 20)));
        }

        protected void drawTitle(Graphics2D g2) {
            g2.setColor(COL_HEADER);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString(title, 25, 35);
        }

        protected void setupGraphics(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawTitle(g2);
        }
    }

    static class FundingGapChart extends BaseChartPanel {
        public FundingGapChart() {
            super("Funding Crisis (Per Capita)");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setupGraphics(g);
            Graphics2D g2 = (Graphics2D) g;
            int startX = 60, bottomY = getHeight() - 60;
            g2.setColor(COL_CHART_URBAN);
            g2.fillRect(startX, 80, 60, bottomY - 80);
            g2.setColor(Color.BLACK);
            g2.drawString("Target: 250/-", startX, 70);
            g2.setColor(COL_ACCENT);
            g2.fillRect(startX + 100, bottomY - 2, 60, 2);
            g2.setColor(Color.BLACK);
            g2.drawString("Actual: 0.15/-", startX + 100, bottomY - 10);
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            g2.drawString("Source: KMH Action Plan", 40, bottomY + 30);
        }
    }

    static class BurdenChart extends BaseChartPanel {
        public BurdenChart() {
            super("Hospital Patients (MH)");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setupGraphics(g);
            Graphics2D g2 = (Graphics2D) g;
            int x = 60, y = 60, w = 180, h = 180;
            g2.setColor(COL_ACCENT);
            g2.fillArc(x, y, w, h, 0, 144);
            g2.setColor(COL_CHART_URBAN);
            g2.fillArc(x, y, w, h, 144, 90);
            g2.setColor(COL_CHART_GAP);
            g2.fillArc(x, y, w, h, 234, 126);
            drawLegend(g2, COL_ACCENT, "40% Inpatients", 260, 100);
            drawLegend(g2, COL_CHART_URBAN, "25% Outpatients", 260, 130);
            drawLegend(g2, COL_CHART_GAP, "Other", 260, 160);
        }
    }

    static class TreatmentGapChart extends BaseChartPanel {
        public TreatmentGapChart() {
            super("Treatment Gap (~75%)");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setupGraphics(g);
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth(), h = getHeight();
            int size = Math.min(w, h) - 120;
            int x = (w - size) / 2, y = (h - size) / 2 + 30;
            int thickness = size / 5;
            g2.setColor(COL_CHART_GAP);
            Area gapArea = new Area(new Arc2D.Double(x, y, size, size, 0, 360, Arc2D.PIE));
            Area innerHole = new Area(
                    new Ellipse2D.Double(x + thickness, y + thickness, size - thickness * 2, size - thickness * 2));
            gapArea.subtract(innerHole);
            g2.fill(gapArea);
            g2.setColor(COL_ACCENT);
            Area treatedArea = new Area(new Arc2D.Double(x, y, size, size, 90, -90, Arc2D.PIE));
            treatedArea.subtract(innerHole);
            g2.fill(treatedArea);
            g2.setColor(COL_HEADER);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
            drawCenteredString(g2, "75%", x + size / 2, y + size / 2 - 10);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2.setColor(Color.GRAY);
            drawCenteredString(g2, "Unmet Need", x + size / 2, y + size / 2 + 20);
        }
    }

    private static void drawLegend(Graphics2D g2, Color c, String text, int x, int y) {
        g2.setColor(c);
        g2.fillRect(x, y, 15, 15);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString(text, x + 25, y + 12);
    }

    private static void drawCenteredString(Graphics2D g2, String text, int x, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, x - fm.stringWidth(text) / 2, y);
    }
}