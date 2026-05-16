package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import database.DataBaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ModernDeliveryLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> roleCombo;

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);

    public ModernDeliveryLogin() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Door2Door - Login Portal");
        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/delivery_logo.png"));
            Image appimg = appIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(appimg);
        } catch (Exception e) {}

setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
setSize(950, 600);
addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        dispose();
        new EndingFrame().setVisible(true);
    }
});
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null) {
            private final Image bgImage = new ImageIcon(getClass().getResource("/images/background.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        mainPanel.setOpaque(false);
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(bgColor);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(45, 70, 45, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("🚚 Door2Door Company");
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(darkText);

        JLabel subtitle = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setForeground(darkText);

        // التعديل: إضافة خيار "Driver" للقائمة
        roleCombo = new JComboBox<>(new String[]{"Admin", "Operations", "Accountant", "Customer Service", "Customer", "Driver"});
        styleCombo(roleCombo);

        txtUsername = new JTextField();
        styleField(txtUsername, "Enter your username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, makeIconLabel(new UserIcon(22, darkText)));

        txtPassword = new JPasswordField();
        styleField(txtPassword, "Enter your password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, makeIconLabel(new LockIcon(22, darkText)));

        char defaultEcho = txtPassword.getEchoChar();
        JButton eyeButton = new JButton(new EyeOffIcon(20, darkText));
        eyeButton.setBorder(null);
        eyeButton.setOpaque(false);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton.setPreferredSize(new Dimension(28, 28));

        eyeButton.addActionListener(e -> {
            if (txtPassword.getEchoChar() == (char) 0) {
                txtPassword.setEchoChar(defaultEcho);
                eyeButton.setIcon(new EyeOffIcon(20, darkText));
            } else {
                txtPassword.setEchoChar((char) 0);
                eyeButton.setIcon(new EyeIcon(20, darkText));
            }
        });

        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, eyeButton);

        JButton btnForgot = new JButton("Forgot Password?");
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setForeground(darkText);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgot.addActionListener(e -> {
            this.setVisible(false);
            new ForgotPasswordFrame(this).setVisible(true);
        });

        JButton btnLogin = new JButton("SIGN IN");
        styleMainButton(btnLogin);
        btnLogin.addActionListener(e -> performLogin());

        JButton btnCreate = new JButton("Create Account");
        btnCreate.setBorderPainted(false);
        btnCreate.setContentAreaFilled(false);
        btnCreate.setForeground(darkText);
        btnCreate.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.addActionListener(e -> createAccount());

        addRow(leftPanel, gbc, title, 0, 0, 10, 0);
        addRow(leftPanel, gbc, subtitle, 0, 0, 30, 0);
        addRow(leftPanel, gbc, createLabel("Role"), 0, 0, 5, 0);
        addRow(leftPanel, gbc, roleCombo, 0, 0, 16, 0);
        addRow(leftPanel, gbc, createLabel("Username"), 0, 0, 5, 0);
        addRow(leftPanel, gbc, txtUsername, 0, 0, 16, 0);
        addRow(leftPanel, gbc, createLabel("Password"), 0, 0, 5, 0);
        addRow(leftPanel, gbc, txtPassword, 0, 0, 10, 0);
        addRow(leftPanel, gbc, btnForgot, 0, 0, 22, 0);
        addRow(leftPanel, gbc, btnLogin, 0, 0, 18, 0);
        addRow(leftPanel, gbc, btnCreate, 0, 0, 0, 0);

        leftPanel.setOpaque(false);
        leftPanel.setBounds(35, 55, 430, 520);

        mainPanel.add(leftPanel);
        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(darkText);
        return label;
    }

    private void styleField(JTextField field, String placeholder) {
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        field.putClientProperty(FlatClientProperties.STYLE, "arc:25; focusWidth:1; innerFocusWidth:0; borderWidth:1");
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.putClientProperty(FlatClientProperties.STYLE, "arc:25; focusWidth:1; innerFocusWidth:0; borderWidth:1");
    }

    private void styleMainButton(JButton button) {
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty(FlatClientProperties.STYLE, "arc:30; borderWidth:0; focusWidth:0");
    }

    private JLabel makeIconLabel(Icon icon) {
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));
        return label;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, Component component, int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
        panel.add(component, gbc);
    }

    private void performLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // التعديل: التوجيه بناءً على الدور
               if ("Driver".equals(role)) {

    new DriverDashboard(user).setVisible(true);

} else if ("Customer Service".equals(role)) {

    new CustomerServiceDashboard(user).setVisible(true);

} else {

    new WelcomeSplash(user, role).setVisible(true);
}
               this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Username, Password or Role!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void createAccount() {
        this.setVisible(false);
        JDialog dialog = new JDialog(this, "Create Account", true);
        dialog.setSize(500, 540);
        dialog.setLocationRelativeTo(this);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) { ModernDeliveryLogin.this.setVisible(true); }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(darkText);

        JTextField usernameField = new JTextField();
        styleField(usernameField, "Enter username");
        usernameField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, makeIconLabel(new UserIcon(22, darkText)));

        JPasswordField passwordField = new JPasswordField();
        styleField(passwordField, "Enter password");
        passwordField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, makeIconLabel(new LockIcon(22, darkText)));

        // التعديل: إضافة خيار "Driver" في إنشاء الحساب
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "Operations", "Accountant", "Customer Service", "Customer", "Driver"});
        styleCombo(roleBox);

        JButton createBtn = new JButton("CREATE ACCOUNT");
        styleMainButton(createBtn);
        createBtn.addActionListener(e -> {
            String newUser = usernameField.getText().trim();
            String newPass = new String(passwordField.getPassword());
            String newRole = (String) roleBox.getSelectedItem();
            if (newUser.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields.");
                return;
            }
            try (Connection conn = DataBaseConnection.getConnection()) {
                String checkSql = "SELECT * FROM users WHERE username=?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, newUser);
                if (checkStmt.executeQuery().next()) {
                    JOptionPane.showMessageDialog(dialog, "Username already exists!");
                    return;
                }
                String insertSql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, newUser);
                pstmt.setString(2, newPass);
                pstmt.setString(3, newRole);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Account created successfully!");
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setForeground(darkText);
        cancelBtn.addActionListener(e -> dialog.dispose());

        addRow(panel, gbc, title, 0, 0, 10, 0);
        addRow(panel, gbc, createLabel("Username"), 0, 0, 5, 0);
        addRow(panel, gbc, usernameField, 0, 0, 18, 0);
        addRow(panel, gbc, createLabel("Password"), 0, 0, 5, 0);
        addRow(panel, gbc, passwordField, 0, 0, 18, 0);
        addRow(panel, gbc, createLabel("Role"), 0, 0, 5, 0);
        addRow(panel, gbc, roleBox, 0, 0, 25, 0);
        addRow(panel, gbc, createBtn, 0, 0, 15, 0);
        addRow(panel, gbc, cancelBtn, 0, 0, 0, 0);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ================= ICONS CLASSES (Keep original classes as they are) =================
    static class UserIcon implements Icon {
        private final int size; private final Color color;
        UserIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; } public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawOval(x + 7, y + 3, 8, 8);
            g2.drawArc(x + 3, y + 11, 16, 13, 0, 180);
            g2.dispose();
        }
    }

    static class LockIcon implements Icon {
        private final int size; private final Color color;
        LockIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; } public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x + 4, y + 10, 14, 10, 3, 3);
            g2.drawArc(x + 6, y + 4, 10, 12, 0, 180);
            g2.fillOval(x + 10, y + 14, 3, 3);
            g2.dispose();
        }
    }

    static class EyeIcon implements Icon {
        private final int size; private final Color color;
        EyeIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; } public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawArc(x + 2, y + 6, size - 4, size - 10, 20, 140);
            g2.drawArc(x + 2, y + 6, size - 4, size - 10, 200, 140);
            g2.drawOval(x + size / 2 - 3, y + size / 2 - 3, 6, 6);
            g2.dispose();
        }
    }

    static class EyeOffIcon implements Icon {
        private final int size; private final Color color;
        EyeOffIcon(int size, Color color) { this.size = size; this.color = color; }
        public int getIconWidth() { return size; } public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawArc(x + 2, y + 6, size - 4, size - 10, 20, 140);
            g2.drawArc(x + 2, y + 6, size - 4, size - 10, 200, 140);
            g2.drawOval(x + size / 2 - 3, y + size / 2 - 3, 6, 6);
            g2.drawLine(x + 4, y + size - 4, x + size - 4, y + 4);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernDeliveryLogin().setVisible(true));
    }
}