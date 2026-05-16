package gui;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class DriverDashboard extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);

    private String driverName;

    public DriverDashboard(String driverName) {

        this.driverName = driverName;

        setTitle("Driver Portal - " + driverName);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(bgColor);

        JLabel welcomeLabel = new JLabel(
                "<html>Welcome,<br>Captain " + driverName + "!</html>"
        );
        welcomeLabel.setBounds(60, 80, 420, 140);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 44));
        welcomeLabel.setForeground(darkText);
        mainPanel.add(welcomeLabel);

        JButton btnViewOrders = createMenuButton("▣", "My Delivery Orders");
        btnViewOrders.setBounds(60, 260, 480, 75);
        mainPanel.add(btnViewOrders);

        JButton btnViewFeedback = createMenuButton("★", "View My Ratings & Feedback");
        btnViewFeedback.setBounds(60, 375, 480, 75);
        mainPanel.add(btnViewFeedback);

        JButton btnLogout = createMenuButton("↪", "Logout");
        btnLogout.setBounds(60, 490, 480, 75);
        mainPanel.add(btnLogout);

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(560, 90, 500, 470);

        try {
            ImageIcon icon = new ImageIcon(
                    getClass().getResource("/images/driver.png")
            );

            Image img = icon.getImage().getScaledInstance(
                    500,
                    470,
                    Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(img));

        } catch (Exception e) {
            imageLabel.setText("🚚");
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 220));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        mainPanel.add(imageLabel);

        JPanel footer = new JPanel(null);
        footer.setBounds(0, 610, 1100, 90);
        footer.setBackground(new Color(180, 105, 145));
        mainPanel.add(footer);

        JLabel thanks = new JLabel("☑  Thank you for delivering excellence!");
        thanks.setBounds(45, 20, 520, 45);
        thanks.setFont(new Font("SansSerif", Font.ITALIC, 22));
        thanks.setForeground(Color.WHITE);
        footer.add(thanks);

        JLabel captain = new JLabel("☻  Captain " + driverName);
        captain.setBounds(820, 20, 260, 45);
        captain.setFont(new Font("SansSerif", Font.BOLD, 22));
        captain.setForeground(Color.WHITE);
        footer.add(captain);

        btnViewOrders.addActionListener(e -> {
            new OrdersManagement("Driver", driverName, this).setVisible(true);
            this.setVisible(false);
        });

        btnViewFeedback.addActionListener(e -> {
            new RatingsManagement("Driver", driverName, this).setVisible(true);
            this.setVisible(false);
        });

        btnLogout.addActionListener(e -> {
            new ModernDeliveryLogin().setVisible(true);
            dispose();
        });

        add(mainPanel);
    }

    private JButton createMenuButton(String icon, String text) {

        JButton btn = new JButton();

        btn.setLayout(new BorderLayout(20, 0));
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 20));

        btn.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25; borderWidth:0; focusWidth:0"
        );

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        iconLabel.setForeground(Color.WHITE);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        textLabel.setForeground(Color.WHITE);

        JLabel arrow = new JLabel("›");
        arrow.setFont(new Font("SansSerif", Font.BOLD, 38));
        arrow.setForeground(Color.WHITE);

        btn.add(iconLabel, BorderLayout.WEST);
        btn.add(textLabel, BorderLayout.CENTER);
        btn.add(arrow, BorderLayout.EAST);

        return btn;
    }
}