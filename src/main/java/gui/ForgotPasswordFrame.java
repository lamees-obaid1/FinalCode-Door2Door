package gui;

import com.formdev.flatlaf.FlatClientProperties;
import database.DataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ForgotPasswordFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField newPasswordField;
    private JFrame loginFrame;

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color fieldColor = new Color(235, 185, 175);

    public ForgotPasswordFrame(JFrame loginFrame) {

        this.loginFrame = loginFrame;

        setTitle("Door2Door - Reset Password");
        setSize(500, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (ForgotPasswordFrame.this.loginFrame != null) {
                    ForgotPasswordFrame.this.loginFrame.setVisible(true);
                }
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 45, 25, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel title = new JLabel("Reset Password", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(darkText);

        JLabel subtitle = new JLabel("Enter your account information", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(darkText);

        JLabel imageLabel;

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/forgotppass.png"));
            Image img = icon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            imageLabel = new JLabel("🔐", SwingConstants.CENTER);
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            imageLabel.setForeground(darkText);
        }

        JLabel usernameLabel = createLabel("Username");
        usernameField = new JTextField();
        styleField(usernameField, "Enter username");
        usernameField.putClientProperty(
                FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT,
                makeIconLabel(new UserIcon(22, darkText))
        );

        JLabel emailLabel = createLabel("Email");
        emailField = new JTextField();
        styleField(emailField, "Enter any valid email");
        emailField.putClientProperty(
                FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT,
                makeIconLabel(new EmailIcon(22, darkText))
        );

        JLabel passwordLabel = createLabel("New Password");
        newPasswordField = new JPasswordField();
        styleField(newPasswordField, "Enter new password");
        newPasswordField.putClientProperty(
                FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT,
                makeIconLabel(new LockIcon(22, darkText))
        );

        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(bgColor);
        showPassword.setForeground(darkText);
        showPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        showPassword.setFocusPainted(false);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                newPasswordField.setEchoChar((char) 0);
            } else {
                newPasswordField.setEchoChar('•');
            }
        });

        JButton resetBtn = new JButton("RESET PASSWORD");
        resetBtn.setBackground(buttonColor);
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetBtn.setPreferredSize(new Dimension(200, 48));
        resetBtn.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:30; borderWidth:0; focusWidth:0"
        );

        resetBtn.addActionListener(e -> resetPassword());

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBorderPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setForeground(darkText);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 15));

        cancelBtn.addActionListener(e -> dispose());

        addRow(panel, gbc, title, 0, 0, 6, 0);
        addRow(panel, gbc, subtitle, 0, 0, 18, 0);
        addRow(panel, gbc, imageLabel, 0, 0, 20, 0);

        addRow(panel, gbc, usernameLabel, 0, 0, 6, 0);
        addRow(panel, gbc, usernameField, 0, 0, 14, 0);

        addRow(panel, gbc, emailLabel, 0, 0, 6, 0);
        addRow(panel, gbc, emailField, 0, 0, 14, 0);

        addRow(panel, gbc, passwordLabel, 0, 0, 6, 0);
        addRow(panel, gbc, newPasswordField, 0, 0, 5, 0);

        addRow(panel, gbc, showPassword, 0, 0, 20, 0);
        addRow(panel, gbc, resetBtn, 0, 0, 12, 0);
        addRow(panel, gbc, cancelBtn, 0, 0, 0, 0);

        add(panel);
    }

    private void resetPassword() {

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email.");
            return;
        }

        try (Connection conn = DataBaseConnection.getConnection()) {

            String checkSql = "SELECT * FROM users WHERE username=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Username does not exist.",
                        "Reset Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String updateSql = "UPDATE users SET password=? WHERE username=?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();

            boolean emailSent = false;

            try {
                EmailService.sendPasswordChangedEmail(email, username, newPassword);
                emailSent = true;
            } catch (Exception ex) {
                System.err.println("SMTP Connection Failed: " + ex.getMessage());
            }

            if (emailSent) {
                JOptionPane.showMessageDialog(
                        this,
                        "Password changed successfully.\nEmail sent successfully."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Password updated successfully,\nbut confirmation email could not be sent."
                );
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage()
            );
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(darkText);
        return label;
    }

    private void styleField(JTextField field, String placeholder) {
        field.setBackground(fieldColor);
        field.setForeground(darkText);
        field.setCaretColor(darkText);

        field.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT,
                placeholder
        );

        field.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:28; height:42; focusColor:#522536; borderColor:#994853"
        );
    }

    private JLabel makeIconLabel(Icon icon) {
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 6));
        return label;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, Component comp,
                        int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
        panel.add(comp, gbc);
    }

    static class UserIcon implements Icon {

        private final int size;
        private final Color color;

        UserIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }

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

        private final int size;
        private final Color color;

        LockIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }

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

    static class EmailIcon implements Icon {

        private final int size;
        private final Color color;

        EmailIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawRoundRect(x + 3, y + 6, 16, 12, 3, 3);
            g2.drawLine(x + 4, y + 7, x + 11, y + 13);
            g2.drawLine(x + 18, y + 7, x + 11, y + 13);

            g2.dispose();
        }
    }
}