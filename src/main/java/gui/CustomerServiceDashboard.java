package gui;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class CustomerServiceDashboard extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color lightBg = new Color(255, 246, 248);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(150, 86, 145);
    private final Color softPurple = new Color(235, 215, 232);
    private final Color cardPink = new Color(255, 240, 244);

    private final String username;

    public CustomerServiceDashboard(String username) {
        this.username = username;

        setTitle("Customer Service Portal - " + username);
        setSize(1000, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel sideBar = createSidebar();
        JPanel contentPanel = createContentPanel();

        add(sideBar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sideBar = new JPanel(null);
        sideBar.setPreferredSize(new Dimension(245, 720));
        sideBar.setBackground(bgColor);

        JPanel logoCircle = createCircleIcon("🎧", 50);
        logoCircle.setBounds(75, 38, 92, 92);
        sideBar.add(logoCircle);

        JLabel portalTitle = new JLabel(
                "<html><center><b>Customer Service</b><br>Portal</center></html>",
                SwingConstants.CENTER
        );
        portalTitle.setBounds(25, 145, 195, 65);
        portalTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        portalTitle.setForeground(darkText);
        sideBar.add(portalTitle);

        JButton complaintsBtn = createSideButton("⚠   View Complaints");
        complaintsBtn.setBounds(18, 260, 210, 52);
        sideBar.add(complaintsBtn);

        JButton ordersBtn = createSideButton("🎁   Track Orders");
        ordersBtn.setBounds(18, 330, 210, 52);
        sideBar.add(ordersBtn);

        JButton feedbackBtn = createSideButton("★   Customer Feedback");
        feedbackBtn.setBounds(18, 400, 210, 52);
        sideBar.add(feedbackBtn);

        JButton logoutBtn = createSideButton("↪   Logout");
        logoutBtn.setBounds(18, 470, 210, 52);
        sideBar.add(logoutBtn);

        JPanel userCircle = createCircleIcon("👤", 30);
        userCircle.setBounds(30, 585, 55, 55);
        sideBar.add(userCircle);

        JLabel userLabel = new JLabel("<html>Welcome,<br><b>" + username + "</b></html>");
        userLabel.setBounds(100, 585, 120, 55);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        userLabel.setForeground(darkText);
        sideBar.add(userLabel);

        complaintsBtn.addActionListener(e -> {
            new ComplaintsManagement("Customer Service", username, this).setVisible(true);
            this.setVisible(false);
        });

        ordersBtn.addActionListener(e -> {
            new OrdersManagement("Customer Service", username, this).setVisible(true);
            this.setVisible(false);
        });

        feedbackBtn.addActionListener(e -> {
            new RatingsAnalytics("Customer Service", username, this).setVisible(true);
            this.setVisible(false);
        });

        logoutBtn.addActionListener(e -> {
            new ModernDeliveryLogin().setVisible(true);
            dispose();
        });

        return sideBar;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(lightBg);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(255, 232, 236));
                g2.fillRect(0, 0, getWidth(), 295);

                g2.setColor(new Color(250, 215, 222));
                int[] x1 = {0, 120, 240, 360, 480, 600, getWidth()};
                int[] y1 = {230, 255, 225, 270, 235, 265, 240};
                g2.fillPolygon(x1, y1, x1.length);

                g2.setColor(new Color(255, 242, 244));
                int[] x2 = {0, 140, 280, 420, 560, getWidth()};
                int[] y2 = {260, 295, 265, 305, 275, 300};
                g2.fillPolygon(x2, y2, x2.length);
            }
        };

        JLabel welcomeTitle = new JLabel("<html>Welcome back,<br>" + username + "!</html>");
        welcomeTitle.setBounds(55, 70, 320, 115);
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 34));
        welcomeTitle.setForeground(darkText);
        contentPanel.add(welcomeTitle);

        JLabel subtitle = new JLabel(
                "<html>We’re here to help you with<br>all your queries and concerns.</html>"
        );
        subtitle.setBounds(55, 195, 320, 65);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setForeground(darkText);
        contentPanel.add(subtitle);

        try {
            ImageIcon supportImg = new ImageIcon(getClass().getResource("/images/com.png"));
            Image scaledImage = supportImg.getImage().getScaledInstance(410, 255, Image.SCALE_SMOOTH);

            JLabel hero = new JLabel(new ImageIcon(scaledImage));
            hero.setBounds(330, 25, 390, 255);
            contentPanel.add(hero);
        } catch (Exception e) {
            JLabel hero = new JLabel("🎧", SwingConstants.CENTER);
            hero.setBounds(420, 55, 250, 210);
            hero.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
            contentPanel.add(hero);
        }

        JLabel quick = new JLabel("Quick Actions");
        quick.setBounds(45, 315, 230, 30);
        quick.setFont(new Font("SansSerif", Font.BOLD, 21));
        quick.setForeground(darkText);
        contentPanel.add(quick);

        JPanel card1 = createCard("⚠", "View Complaints", "Check the status and\ndetails of complaints.");
        card1.setBounds(45, 365, 155, 180);
        contentPanel.add(card1);

        JPanel card2 = createCard("🎁", "Track Orders", "Track orders and\ncheck delivery status.");
        card2.setBounds(220, 365, 155, 180);
        contentPanel.add(card2);

        JPanel card3 = createCard("★", "Feedback", "Share feedback to\nhelp us improve.");
        card3.setBounds(395, 365, 155, 180);
        contentPanel.add(card3);

        JPanel card4 = createCard("🎧", "Need Help?", "Contact support\nteam anytime.");
        card4.setBounds(570, 365, 155, 180);
        contentPanel.add(card4);

        JPanel supportBox = new JPanel(null);
        supportBox.setBackground(softPurple);
        supportBox.setBounds(45, 570, 680, 70);
        supportBox.putClientProperty(FlatClientProperties.STYLE, "arc:22");

        JLabel supportIcon = new JLabel("🎧", SwingConstants.CENTER);
        supportIcon.setBounds(15, 12, 45, 45);
        supportIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        supportBox.add(supportIcon);

        JLabel supportText = new JLabel(
                "<html><b>We’re here for you!</b><br>Our support team is ready to assist you 24/7.</html>"
        );
        supportText.setBounds(75, 10, 350, 50);
        supportText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        supportText.setForeground(darkText);
        supportBox.add(supportText);

        JButton contactBtn = createSmallButton("💬 Contact Support");
        contactBtn.setBounds(485, 18, 170, 36);
        supportBox.add(contactBtn);

        contentPanel.add(supportBox);

        return contentPanel;
    }

    private JPanel createCircleIcon(String icon, int fontSize) {
        JPanel circle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(buttonColor);
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, fontSize));

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                g2.drawString(icon, x, y);
            }
        };

        circle.setOpaque(false);
        return circle;
    }

    private JButton createSideButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:15; borderWidth:0; focusWidth:0");
        return btn;
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:15; borderWidth:0; focusWidth:0");
        return btn;
    }

    private JPanel createCard(String icon, String title, String desc) {
        JPanel card = new JPanel(null);
        card.setBackground(cardPink);
        card.putClientProperty(FlatClientProperties.STYLE, "arc:22");

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setBounds(50, 15, 55, 55);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(softPurple);
        iconLabel.setForeground(buttonColor);
        iconLabel.putClientProperty(FlatClientProperties.STYLE, "arc:55");
        card.add(iconLabel);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setBounds(8, 80, 140, 25);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(darkText);
        card.add(titleLabel);

        JLabel descLabel = new JLabel(
                "<html><center>" + desc.replace("\n", "<br>") + "</center></html>",
                SwingConstants.CENTER
        );
        descLabel.setBounds(10, 108, 135, 43);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setForeground(darkText);
        card.add(descLabel);

        JLabel arrow = new JLabel("→", SwingConstants.CENTER);
        arrow.setBounds(55, 150, 45, 24);
        arrow.setFont(new Font("SansSerif", Font.BOLD, 23));
        arrow.setForeground(buttonColor);
        card.add(arrow);

        return card;
    }
}