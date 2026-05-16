package gui;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import database.DataBaseConnection;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.util.Locale;

public class Dashboard extends JFrame {

    // ===== COLORS =====
    private final Color bgColor = new Color(214, 164, 156);
    private final Color darkText = new Color(72, 40, 55);
    private final Color buttonColor = new Color(165, 95, 160);
    private final Color cardColor = new Color(255, 245, 242);

    private JLabel lblOrders, lblDrivers, lblRevenue;

    private String user;
    private String role;

    public Dashboard(String user, String role) {

        this.user = user;
        this.role = role;

        setTitle("Door2Door - Dashboard");

        try {

            ImageIcon appIcon =
                    new ImageIcon(
                            getClass().getResource("/images/delivery_logo.png"));

            Image appImg =
                    appIcon.getImage().getScaledInstance(
                            120,
                            120,
                            Image.SCALE_SMOOTH);

            setIconImage(appImg);

        } catch (Exception e) {
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1350, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================

        JPanel sideBar = new JPanel(null);

        sideBar.setPreferredSize(new Dimension(230, 900));

        sideBar.setBackground(new Color(194, 116, 128));

        JLabel logoImage = new JLabel();
        logoImage.setBounds(70, 25, 90, 90);

        try {

            ImageIcon icon =
                    new ImageIcon(
                            getClass().getResource("/images/delivery_logo.png"));

            Image img =
                    icon.getImage().getScaledInstance(
                            85,
                            85,
                            Image.SCALE_SMOOTH);

            logoImage.setIcon(new ImageIcon(img));

        } catch (Exception e) {

            logoImage.setText("🚚");

            logoImage.setFont(
                    new Font("Segoe UI Emoji",
                            Font.PLAIN,
                            55));

            logoImage.setForeground(Color.WHITE);
        }

        sideBar.add(logoImage);

        JLabel logoText =
                new JLabel(
                        "Door2Door",
                        SwingConstants.CENTER);

        logoText.setBounds(30, 120, 170, 35);

        logoText.setForeground(Color.WHITE);

        logoText.setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        22));

        sideBar.add(logoText);

        // ===== SIDEBAR BUTTONS =====

addSideButton(sideBar, "🏠 Dashboard", 165, true, null);

int y = 225;

// ================= ADMIN =================

if (role.equalsIgnoreCase("Admin")) {

    addSideButton(sideBar, "⚠ Complaints", y, false, e -> {
        new ComplaintsManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "⭐ Ratings", y, false, e -> {
        new RatingsAnalytics(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "📦 Orders", y, false, e -> {
        new OrdersManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🚚 Drivers", y, false, e -> {
        new DriversManagement(this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "👥 Customers", y, false, e -> {
        new CustomersManagement(role, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "💳 Payments", y, false, e -> {
        new PaymentsManagement(role, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🧾 Invoices", y, false, e -> {
        new InvoicesManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🚛 Vehicles", y, false, e -> {
        new VehiclesManagement(this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🔍 Tracking", y, false, e -> {
        new OrderTracking(this).setVisible(true);
        this.setVisible(false);
    });
}

// ================= OPERATIONS =================

else if (role.equalsIgnoreCase("Operations")) {

    addSideButton(sideBar, "📦 Orders", y, false, e -> {
        new OrdersManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🚚 Drivers", y, false, e -> {
        new DriversManagement(this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🔍 Tracking", y, false, e -> {
        new OrderTracking(this).setVisible(true);
        this.setVisible(false);
    });
}

// ================= ACCOUNTANT =================

else if (role.equalsIgnoreCase("Accountant")) {

    addSideButton(sideBar, "💳 Payments", y, false, e -> {
        new PaymentsManagement(role, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🧾 Invoices", y, false, e -> {
        new InvoicesManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });
}

// ================= CUSTOMER SERVICE =================

else if (role.equalsIgnoreCase("Customer Service")) {

    addSideButton(sideBar, "👥 Customers", y, false, e -> {
        new CustomersManagement(role, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🔍 Tracking", y, false, e -> {
        new OrderTracking(this).setVisible(true);
        this.setVisible(false);
    });
}

// ================= CUSTOMER =================

else if (role.equalsIgnoreCase("Customer")) {

    addSideButton(sideBar, "📦 My Orders", y, false, e -> {
        new OrdersManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "📍 Track Package", y, false, e -> {
        new OrderTracking(this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "🧾 My Invoices", y, false, e -> {
        new InvoicesManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "⚠ Complaints", y, false, e -> {
        new ComplaintsManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });

    y += 55;

    addSideButton(sideBar, "⭐ Ratings", y, false, e -> {
        new RatingsManagement(role, user, this).setVisible(true);
        this.setVisible(false);
    });
}
        // ===== LOGOUT =====

        JButton logout = new JButton("↪ Logout");

      
logout.setBounds(25, 730, 180, 48);
        logout.setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        16));

        logout.setBackground(buttonColor);

        logout.setForeground(Color.WHITE);

        logout.setFocusPainted(false);

        logout.setBorderPainted(false);

        logout.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25");

        logout.addActionListener(e -> {

            dispose();

            new ModernDeliveryLogin().setVisible(true);
        });

        sideBar.add(logout);

        add(sideBar, BorderLayout.WEST);

        // ================= MAIN PANEL =================

        JPanel mainPanel =
                new JPanel(new BorderLayout(20, 20));

        mainPanel.setBackground(cardColor);

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        30,
                        30,
                        30));

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome back, "
                                + user
                                + " ("
                                + role
                                + ")");

        welcomeLabel.setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        38));

        welcomeLabel.setForeground(darkText);

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // ================= CENTER PANEL =================

        JPanel centerPanel =
                new JPanel(new BorderLayout(0, 30));

        centerPanel.setOpaque(false);

        // ================= STATS =================

        JPanel statsPanel =
                new JPanel(new GridLayout(1, 3, 20, 0));

        statsPanel.setOpaque(false);

        lblOrders =
                new JLabel("0",
                        SwingConstants.CENTER);

        lblDrivers =
                new JLabel("0",
                        SwingConstants.CENTER);

        lblRevenue =
                new JLabel("$0",
                        SwingConstants.CENTER);

        Font statFont =
                new Font("SansSerif",
                        Font.BOLD,
                        34);

        lblOrders.setFont(statFont);
        lblDrivers.setFont(statFont);
        lblRevenue.setFont(statFont);

        statsPanel.add(
                createStatCard(
                        "Total Orders",
                        lblOrders));

        statsPanel.add(
                createStatCard(
                        "Active Drivers",
                        lblDrivers));

        if (role.equalsIgnoreCase("Admin")
                || role.equalsIgnoreCase("Accountant")) {

            statsPanel.add(
                    createStatCard(
                            "Revenue",
                            lblRevenue));
        }

          // ===== STATS =====

centerPanel.add(statsPanel, BorderLayout.NORTH);

// ===== CHARTS =====

JPanel chartsPanel =
        new JPanel(new GridLayout(1, 2, 25, 0));

chartsPanel.setOpaque(false);

chartsPanel.setPreferredSize(
        new Dimension(950, 420));

chartsPanel.add(createOrderStatsChart());

chartsPanel.add(createRevenueBarChart());

centerPanel.add(
        chartsPanel,
        BorderLayout.CENTER);
        
        
        
        
        

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        fetchStats(role);
    }

    // ================= PIE CHART =================

    private JPanel createOrderStatsChart() {

        DefaultPieDataset dataset =
                new DefaultPieDataset();

        try (
                Connection conn =
                        DataBaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(
                                "SELECT order_status, COUNT(*) as count FROM orders GROUP BY order_status")
        ) {

            while (rs.next()) {

                dataset.setValue(
                        rs.getString("order_status"),
                        rs.getInt("count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart =
                ChartFactory.createPieChart(
                        "Orders Status Distribution",
                        dataset,
                        true,
                        true,
                        false);

        styleChart(chart);

        PiePlot plot =
                (PiePlot) chart.getPlot();

        plot.setSectionPaint(
                "Delivered",
                new Color(46, 130, 80));

        plot.setSectionPaint(
                "Pending",
                new Color(193, 120, 145));

        ChartPanel panel =
                new ChartPanel(chart);

        panel.setBackground(Color.WHITE);

        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25");

        return panel;
    }

    // ================= BAR CHART =================

    private JPanel createRevenueBarChart() {

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        try (
                Connection conn =
                        DataBaseConnection.getConnection();

                Statement stmt =
                        conn.createStatement();

                ResultSet rs =
                        stmt.executeQuery(
                                "SELECT branch_id, SUM(price_paid) as total FROM payment p JOIN orders o ON p.invoice_id = o.order_id GROUP BY branch_id")
        ) {

            while (rs.next()) {

                dataset.addValue(
                        rs.getDouble("total"),
                        "Revenue",
                        "Branch "
                                + rs.getInt("branch_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart =
                ChartFactory.createBarChart(
                        "Revenue per Branch",
                        "Branch",
                        "Total ($)",
                        dataset);

        styleChart(chart);

        CategoryPlot plot =
                (CategoryPlot) chart.getPlot();

        plot.getRenderer().setSeriesPaint(
                0,
                buttonColor);

        ChartPanel panel =
                new ChartPanel(chart);

        panel.setBackground(Color.WHITE);

        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25");

        return panel;
    }

    private void styleChart(JFreeChart chart) {

        chart.setBackgroundPaint(Color.WHITE);

        chart.getTitle().setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        18));

        chart.getPlot().setBackgroundPaint(Color.WHITE);

        chart.getPlot().setOutlineVisible(false);
    }

    // ================= STAT CARD =================

    private JPanel createStatCard(
            String title,
            JLabel valueLabel) {

        JPanel card =
                new JPanel(new BorderLayout());

        card.setBackground(buttonColor);

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20));

        card.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25");

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER);

        titleLabel.setFont(
                new Font("SansSerif",
                        Font.PLAIN,
                        15));

        titleLabel.setForeground(Color.WHITE);

        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);

        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= SIDEBAR BUTTON =================

    private void addSideButton(
            JPanel panel,
            String text,
            int y,
            boolean active,
            java.awt.event.ActionListener action) {

        JButton btn = new JButton(text);

        btn.setBounds(15, y, 190, 48);

        btn.setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        16));

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        btn.setHorizontalAlignment(
                SwingConstants.LEFT);

        btn.setMargin(
                new Insets(0, 18, 0, 0));

        if (active) {

            btn.setBackground(
                    new Color(165, 95, 160));

        } else {

            btn.setBackground(
                    new Color(193, 120, 145));
        }

        btn.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent evt) {

                        if (!active) {

                            btn.setBackground(
                                    new Color(165, 95, 160));
                        }
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent evt) {

                        if (!active) {

                            btn.setBackground(
                                    new Color(193, 120, 145));
                        }
                    }
                });

        if (action != null) {
            btn.addActionListener(action);
        }

        panel.add(btn);
    }

    // ================= FETCH STATS =================

    private void fetchStats(String role) {

        if (role.equalsIgnoreCase("Customer"))
            return;

        try (
                Connection conn =
                        DataBaseConnection.getConnection()
        ) {

            ResultSet rs1 =
                    conn.createStatement()
                            .executeQuery(
                                    "SELECT COUNT(*) FROM orders");

            if (rs1.next()) {

                lblOrders.setText(
                        String.valueOf(rs1.getInt(1)));
            }

            ResultSet rs2 =
                    conn.createStatement()
                            .executeQuery(
                                    "SELECT COUNT(*) FROM driver");

            if (rs2.next()) {

                lblDrivers.setText(
                        String.valueOf(rs2.getInt(1)));
            }

            if (role.equalsIgnoreCase("Admin")
                    || role.equalsIgnoreCase("Accountant")) {

                ResultSet rs3 =
                        conn.createStatement()
                                .executeQuery(
                                        "SELECT SUM(price_paid) FROM payment");

                if (rs3.next()) {

                    lblRevenue.setText(
                            "$"
                                    + String.format(
                                    Locale.US,
                                    "%.0f",
                                    rs3.getDouble(1)));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}