package gui;

import com.formdev.flatlaf.FlatClientProperties;
import database.DataBaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RatingsManagement extends JFrame {

    private final Color bgColor = new Color(214, 164, 156);
    private final Color darkText = new Color(72, 40, 55);
    private final Color buttonColor = new Color(165, 95, 160);
    private final Color cardColor = new Color(255, 248, 245);
    private final Color starColor = new Color(245, 151, 57);
    private final Color emptyStarColor = new Color(205, 200, 210);
    private final Color iconBg = new Color(232, 205, 235);

    private String role;
    private String user;
    private JFrame parentFrame;

    private StarRating serviceStars;
    private StarRating driverStars;
    private StarRating parcelStars;
    private StarRating speedStars;
    private StarRating communicationStars;
    private StarRating priceStars;
    private StarRating overallStars;

    private JTextArea feedbackArea;

    private JTable ratingsTable;
    private DefaultTableModel tableModel;

    public RatingsManagement(String role, String user, JFrame parentFrame) {

        this.role = role;
        this.user = user;
        this.parentFrame = parentFrame;

        setTitle("Door2Door - Ratings");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new BackgroundPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(35, 45, 30, 45));

        if (role.equalsIgnoreCase("Customer")) {
            main.add(createCustomerView(), BorderLayout.CENTER);
        } else {
            main.add(createAdminView(), BorderLayout.CENTER);
            loadRatings();
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        bottomPanel.setOpaque(false);

        if (role.equalsIgnoreCase("Customer")) {
            JButton submitBtn = createButton("✈  Submit Rating");
            submitBtn.addActionListener(e -> submitRating());
            bottomPanel.add(submitBtn);
        }

        JButton backBtn = createButton("←  Back");
        backBtn.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });

        bottomPanel.add(backBtn);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
    }

    private JPanel createCustomerView() {

        JPanel content = new JPanel(new BorderLayout(35, 0));
        content.setOpaque(false);

        JPanel leftPanel = createLeftPanel();

        JPanel card = new RoundedPanel();
        card.setBackground(cardColor);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel rowsPanel = new JPanel();
        rowsPanel.setOpaque(false);
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

      serviceStars = new StarRating(0);
driverStars = new StarRating(0);
parcelStars = new StarRating(0);
speedStars = new StarRating(0);
communicationStars = new StarRating(0);
priceStars = new StarRating(0);
overallStars = new StarRating(0);

        rowsPanel.add(createRatingRow("⏰", "Service Rating  *", serviceStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createRatingRow("🚚", "Driver Rating", driverStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createRatingRow("📦", "Parcel Rating", parcelStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createRatingRow("⚡", "Delivery Speed", speedStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createRatingRow("☎", "Communication", communicationStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createRatingRow("💰", "Price Rating", priceStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createRatingRow("🏅", "Overall Rating", overallStars));
        rowsPanel.add(createSeparator());
        rowsPanel.add(createFeedbackRow());

        card.add(rowsPanel, BorderLayout.CENTER);

        content.add(leftPanel, BorderLayout.WEST);
        content.add(card, BorderLayout.CENTER);

        return content;
    }

    private JPanel createLeftPanel() {

        JPanel left = new JPanel(null);
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(410, 600));

        JLabel title = new JLabel("<html>Rate Our Service</html>");
        title.setFont(new Font("SansSerif", Font.BOLD, 42));
        title.setForeground(darkText);
        title.setBounds(0, 25, 380, 60);
        left.add(title);

        JLabel underline = new JLabel();
        underline.setOpaque(true);
        underline.setBackground(buttonColor);
        underline.setBounds(5, 95, 95, 4);
        left.add(underline);

        JLabel desc = new JLabel("<html>Your feedback helps us<br>improve and serve you<br>better every day.</html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 21));
        desc.setForeground(darkText);
        desc.setBounds(5, 120, 350, 115);
        left.add(desc);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/rating.png"));
            Image img = icon.getImage().getScaledInstance(380, 360, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(img));
            image.setBounds(0, 250, 390, 360);
            left.add(image);
        } catch (Exception e) {
            JLabel imageText = new JLabel("⭐ ⭐ ⭐ ⭐ ⭐", SwingConstants.CENTER);
            imageText.setFont(new Font("SansSerif", Font.BOLD, 40));
            imageText.setForeground(starColor);
            imageText.setBounds(15, 350, 360, 70);
            left.add(imageText);
        }

        return left;
    }

    private JPanel createRatingRow(String icon, String labelText, StarRating stars) {

        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(720, 68));
        row.setPreferredSize(new Dimension(720, 68));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(iconBg);
        iconLabel.setForeground(buttonColor);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        iconLabel.setPreferredSize(new Dimension(52, 52));
        iconLabel.putClientProperty(FlatClientProperties.STYLE, "arc:999");

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        label.setForeground(darkText);
        label.setPreferredSize(new Dimension(230, 50));

        JPanel leftPart = new JPanel(new BorderLayout(15, 0));
        leftPart.setOpaque(false);
        leftPart.add(iconLabel, BorderLayout.WEST);
        leftPart.add(label, BorderLayout.CENTER);

        row.add(leftPart, BorderLayout.WEST);
        row.add(stars, BorderLayout.CENTER);

        return row;
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(720, 1));
        sep.setForeground(new Color(230, 220, 220));
        return sep;
    }

    private JPanel createFeedbackRow() {

        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(720, 95));
        row.setPreferredSize(new Dimension(720, 95));

        JLabel iconLabel = new JLabel("💬", SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(iconBg);
        iconLabel.setForeground(buttonColor);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setPreferredSize(new Dimension(52, 52));
        iconLabel.putClientProperty(FlatClientProperties.STYLE, "arc:999");

        JPanel center = new JPanel(new BorderLayout(0, 7));
        center.setOpaque(false);

        JLabel label = new JLabel("Feedback  (Optional)");
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        label.setForeground(darkText);

        feedbackArea = new JTextArea();
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        feedbackArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JScrollPane scroll = new JScrollPane(feedbackArea);
        scroll.setPreferredSize(new Dimension(530, 55));
        scroll.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:15; borderWidth:1; focusWidth:1"
        );

        center.add(label, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        row.add(iconLabel, BorderLayout.WEST);
        row.add(center, BorderLayout.CENTER);

        return row;
    }

    private JPanel createAdminView() {

        JPanel main = new JPanel(new BorderLayout(0, 25));
        main.setOpaque(false);

        JLabel title = new JLabel("Customer Ratings", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(darkText);

        String[] columns = {
                "ID", "Customer", "Service", "Driver", "Parcel",
                "Speed", "Communication", "Price", "Overall", "Feedback", "Created At"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ratingsTable = new JTable(tableModel);
        ratingsTable.setRowHeight(42);
        ratingsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ratingsTable.setForeground(darkText);
        ratingsTable.setBackground(Color.WHITE);
        ratingsTable.setSelectionBackground(new Color(240, 225, 235));
        ratingsTable.getTableHeader().setBackground(new Color(222, 190, 238));
        ratingsTable.getTableHeader().setForeground(darkText);
        ratingsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(ratingsTable);
        scroll.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25; borderWidth:0; focusWidth:0"
        );

        JButton refresh = createButton("Refresh");
        refresh.addActionListener(e -> loadRatings());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(refresh);

        RoundedPanel card = new RoundedPanel();
        card.setBackground(cardColor);
        card.setLayout(new BorderLayout(15, 15));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.add(scroll, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        main.add(title, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);

        return main;
    }

    private JButton createButton(String text) {

        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 45));

        btn.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25; borderWidth:0; focusWidth:0"
        );

        return btn;
    }

    private void submitRating() {

        String sql =
                "INSERT INTO rating(" +
                        "customer_id, service_rating, driver_rating, parcel_rating, " +
                        "speed_rating, communication_rating, price_rating, overall_rating, " +
                        "feedback, created_at" +
                        ") VALUES (?,?,?,?,?,?,?,?,?,NOW())";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            int customerId = getCustomerId();

            if (customerId == -1) {
                JOptionPane.showMessageDialog(this, "Customer ID not found.");
                return;
            }

            ps.setInt(1, customerId);
            ps.setInt(2, serviceStars.getRating());
            ps.setInt(3, driverStars.getRating());
            ps.setInt(4, parcelStars.getRating());
            ps.setInt(5, speedStars.getRating());
            ps.setInt(6, communicationStars.getRating());
            ps.setInt(7, priceStars.getRating());
            ps.setInt(8, overallStars.getRating());
            ps.setString(9, feedbackArea.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Rating submitted successfully!");
            feedbackArea.setText("");
            serviceStars.reset();
driverStars.reset();
parcelStars.reset();
speedStars.reset();
communicationStars.reset();
priceStars.reset();
overallStars.reset();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Submit Error: " + e.getMessage());
        }
    }

    private int getCustomerId() {

        String sql = "SELECT customer_id FROM users WHERE username=?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("customer_id");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Customer Error: " + e.getMessage());
        }

        return -1;
    }

    private void loadRatings() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        String sql =
                "SELECT evaluation_id, customer_id, service_rating, driver_rating, parcel_rating, " +
                        "speed_rating, communication_rating, price_rating, overall_rating, feedback, created_at " +
                        "FROM rating ORDER BY created_at DESC";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("evaluation_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("service_rating"),
                        rs.getInt("driver_rating"),
                        rs.getInt("parcel_rating"),
                        rs.getInt("speed_rating"),
                        rs.getInt("communication_rating"),
                        rs.getInt("price_rating"),
                        rs.getInt("overall_rating"),
                        rs.getString("feedback"),
                        rs.getTimestamp("created_at")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    class StarRating extends JPanel {
public void reset() {

    rating = 0;
    updateStars();

}
        private int rating;
        private final JLabel[] stars = new JLabel[5];

        public StarRating(int defaultRating) {

            this.rating = defaultRating;

            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));

            for (int i = 0; i < 5; i++) {

                final int value = i + 1;

                JLabel star = new JLabel("★");
                star.setFont(new Font("SansSerif", Font.BOLD, 32));
                star.setCursor(new Cursor(Cursor.HAND_CURSOR));

                star.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        rating = value;
                        updateStars();
                    }
                });

                stars[i] = star;
                add(star);
            }

            updateStars();
        }

        public int getRating() {
            return rating;
        }

        private void updateStars() {

            for (int i = 0; i < stars.length; i++) {
                if (i < rating) {
                    stars[i].setForeground(starColor);
                } else {
                    stars[i].setForeground(emptyStarColor);
                }
            }
        }
    }

    class RoundedPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g2.dispose();

            super.paintComponent(g);
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    }

    class BackgroundPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            g2.setColor(bgColor);
            g2.fillRect(0, 0, w, h);

            g2.setColor(cardColor);
            g2.fillRoundRect(0, 0, w, h - 70, 30, 30);

            g2.setColor(new Color(190, 105, 100));
            Polygon wave = new Polygon();

            wave.addPoint(0, h - 130);
            wave.addPoint(200, h - 165);
            wave.addPoint(400, h - 120);
            wave.addPoint(650, h - 150);
            wave.addPoint(900, h - 105);
            wave.addPoint(w, h - 155);
            wave.addPoint(w, h);
            wave.addPoint(0, h);

            g2.fillPolygon(wave);

            g2.dispose();
        }
    }
}