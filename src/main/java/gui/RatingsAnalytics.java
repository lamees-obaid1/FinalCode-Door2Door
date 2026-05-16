package gui;

import com.formdev.flatlaf.FlatClientProperties;
import database.DataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RatingsAnalytics extends JFrame {

    private final Color bgColor = new Color(214, 164, 156);
    private final Color darkText = new Color(72, 40, 55);
    private final Color buttonColor = new Color(165, 95, 160);
    private final Color cardColor = new Color(255, 248, 245);

    private String role;
    private String user;
    private JFrame parentFrame;

    private JLabel avgServiceLabel;
    private JLabel bestDriverLabel;
    private JLabel complaintsLabel;
    private JLabel avgOverallLabel;

    public RatingsAnalytics(String role, String user, JFrame parentFrame) {

        this.role = role;
        this.user = user;
        this.parentFrame = parentFrame;

        setTitle("Door2Door - Ratings Analytics");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(bgColor);
        main.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JLabel title = new JLabel("Ratings Analytics", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(darkText);

        JPanel cards = new JPanel(new GridLayout(2, 2, 25, 25));
        cards.setOpaque(false);

        avgServiceLabel = new JLabel("0.0 / 5", SwingConstants.CENTER);
        bestDriverLabel = new JLabel("N/A", SwingConstants.CENTER);
        complaintsLabel = new JLabel("0", SwingConstants.CENTER);
        avgOverallLabel = new JLabel("0.0 / 5", SwingConstants.CENTER);

        cards.add(createCard("Average Service Rating", "⭐", avgServiceLabel));
        cards.add(createCard("Best Driver", "🚚", bestDriverLabel));
        cards.add(createCard("Total Complaints", "⚠", complaintsLabel));
        cards.add(createCard("Average Overall Rating", "🌟", avgOverallLabel));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);

        JButton refreshBtn = createButton("Refresh");
        JButton backBtn = createButton("Back");

        refreshBtn.addActionListener(e -> loadAnalytics());

        backBtn.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });

        bottom.add(refreshBtn);
        bottom.add(backBtn);

        main.add(title, BorderLayout.NORTH);
        main.add(cards, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });

        loadAnalytics();
    }

    private JPanel createCard(String title, String icon, JLabel valueLabel) {

    JPanel card = new JPanel(new BorderLayout(10, 10));
    card.setBackground(cardColor);
    card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
    card.putClientProperty(
            FlatClientProperties.STYLE,
            "arc:25"
    );

    JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
    iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));

    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setForeground(darkText);

    valueLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
    valueLabel.setForeground(buttonColor);

    card.add(iconLabel, BorderLayout.NORTH);
    card.add(titleLabel, BorderLayout.CENTER);
    card.add(valueLabel, BorderLayout.SOUTH);

    return card;
}

    private JButton createButton(String text) {

        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));

        btn.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25; borderWidth:0; focusWidth:0"
        );

        return btn;
    }

    private void loadAnalytics() {

        loadAverageService();
        loadAverageOverall();
        loadTotalComplaints();
        loadBestDriver();
    }

    private void loadAverageService() {

        String sql = "SELECT AVG(service_rating) AS avg_service FROM rating";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                double avg = rs.getDouble("avg_service");
                avgServiceLabel.setText(
        String.format(java.util.Locale.US,"%.1f / 5", avg)
);
            }

        } catch (SQLException e) {
            avgServiceLabel.setText("Error");
        }
    }

    private void loadAverageOverall() {

        String sql = "SELECT AVG(overall_rating) AS avg_overall FROM rating";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                double avg = rs.getDouble("avg_overall");
                avgOverallLabel.setText(
        String.format(java.util.Locale.US,"%.1f / 5", avg)
);
            }

        } catch (SQLException e) {
            avgOverallLabel.setText("Error");
        }
    }

    private void loadTotalComplaints() {

        String sql = "SELECT COUNT(*) AS total_complaints FROM complaint";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                complaintsLabel.setText(String.valueOf(rs.getInt("total_complaints")));
            }

        } catch (SQLException e) {
            complaintsLabel.setText("Error");
        }
    }

    private void loadBestDriver() {

        String sql =
                "SELECT driver_id, AVG(driver_rating) AS avg_driver " +
                "FROM rating " +
                "WHERE driver_id IS NOT NULL " +
                "GROUP BY driver_id " +
                "ORDER BY avg_driver DESC " +
                "LIMIT 1";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                bestDriverLabel.setText(
                        "Driver ID " + rs.getInt("driver_id") +
                                " - " + String.format("%.1f", rs.getDouble("avg_driver")) + "/5"
                );
            } else {
                bestDriverLabel.setText("No driver ratings");
            }

        } catch (SQLException e) {
            bestDriverLabel.setText("No data");
        }
    }
}