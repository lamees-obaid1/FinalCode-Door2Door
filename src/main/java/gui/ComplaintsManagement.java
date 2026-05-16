package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ComplaintsManagement extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color lightCard = new Color(255, 248, 245);
    private final Color tableHeader = new Color(222, 190, 238);

    private String role;
    private String username;
    private JFrame parentFrame;

    private JComboBox<String> complaintTypeBox;
    private JTextArea messageArea;
    private JTable complaintsTable;
    private DefaultTableModel tableModel;

    public ComplaintsManagement(String role, String username, JFrame parentFrame) {

        this.role = role;
        this.username = username;
        this.parentFrame = parentFrame;

        setTitle("Door2Door - Complaints");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new BackgroundPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));

        JPanel content = new JPanel(new BorderLayout(30, 0));
        content.setOpaque(false);

        content.add(createLeftPanel(), BorderLayout.WEST);
        content.add(createRightPanel(), BorderLayout.CENTER);

        JButton backBtn = createButton("←  Back");
        backBtn.setPreferredSize(new Dimension(130, 42));

        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(backBtn);

        main.add(content, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parentFrame != null) parentFrame.setVisible(true);
            }
        });
    }

    private JPanel createLeftPanel() {

        JPanel left = new JPanel(null);
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(370, 560));

        JLabel bigText = new JLabel("<html>We’re here<br>to listen.</html>");
        bigText.setFont(new Font("SansSerif", Font.BOLD, 42));
        bigText.setForeground(darkText);
        bigText.setBounds(25, 30, 330, 130);
        left.add(bigText);

        JLabel subText = new JLabel("<html>Submit your complaint and<br>we’ll take care of the rest.</html>");
        subText.setFont(new Font("SansSerif", Font.PLAIN, 19));
        subText.setForeground(darkText);
        subText.setBounds(25, 165, 320, 70);
        left.add(subText);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/compaint.png"));
            Image img = icon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            JLabel image = new JLabel(new ImageIcon(img));
            image.setBounds(0, 260, 360, 350);
            left.add(image);
        } catch (Exception e) {
            JLabel fallback = new JLabel("Support Image");
            fallback.setFont(new Font("SansSerif", Font.BOLD, 22));
            fallback.setForeground(buttonColor);
            fallback.setBounds(60, 350, 250, 40);
            left.add(fallback);
        }

        return left;
    }

    private JPanel createRightPanel() {

        JPanel right = new JPanel(new BorderLayout(0, 25));
        right.setOpaque(false);

        JLabel title = new JLabel(
                role.equalsIgnoreCase("Customer") ? "Submit Complaint" : "Complaints Management",
                SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(darkText);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(createStepsPanel(), BorderLayout.CENTER);

        right.add(top, BorderLayout.NORTH);

        JPanel card = new RoundedPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(lightCard);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        if (role.equalsIgnoreCase("Customer")) {
            card.add(createCustomerForm(), BorderLayout.CENTER);
        } else {
            card.add(createAdminTable(), BorderLayout.CENTER);
            loadComplaints();
        }

        right.add(card, BorderLayout.CENTER);

        return right;
    }

    private JPanel createStepsPanel() {

        JPanel steps = new JPanel(null);
        steps.setOpaque(false);
        steps.setPreferredSize(new Dimension(700, 95));

        addStep(steps, "1", "Select Type", 100, true);
        addStep(steps, "2", "Provide Details", 320, false);
        addStep(steps, "3", "Review & Submit", 540, false);

        JLabel line1 = new JLabel();
        line1.setOpaque(true);
        line1.setBackground(buttonColor);
        line1.setBounds(140, 35, 150, 5);
        steps.add(line1);

        JLabel line2 = new JLabel();
        line2.setOpaque(true);
        line2.setBackground(new Color(225, 215, 215));
        line2.setBounds(360, 35, 150, 5);
        steps.add(line2);

        return steps;
    }

    private void addStep(JPanel panel, String number, String text, int x, boolean active) {

        JLabel circle = new JLabel(number, SwingConstants.CENTER);
        circle.setOpaque(true);
        circle.setBackground(active ? buttonColor : new Color(230, 220, 218));
        circle.setForeground(active ? Color.WHITE : darkText);
        circle.setFont(new Font("SansSerif", Font.BOLD, 18));
        circle.setBounds(x, 15, 55, 55);
        circle.putClientProperty(FlatClientProperties.STYLE, "arc:999");
        panel.add(circle);

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(darkText);
        label.setBounds(x - 45, 70, 145, 25);
        panel.add(label);
    }

    private JPanel createCustomerForm() {

        JPanel form = new JPanel(null);
        form.setOpaque(false);

        JLabel typeLabel = createLabel("Complaint Type:");
        typeLabel.setBounds(80, 35, 170, 30);
        form.add(typeLabel);

        complaintTypeBox = new JComboBox<>(new String[]{"Parcel Complaint", "Driver Complaint"});
        complaintTypeBox.setFont(new Font("SansSerif", Font.PLAIN, 15));
        complaintTypeBox.setBounds(260, 35, 360, 35);
        form.add(complaintTypeBox);

        messageArea = new JTextArea();
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(messageArea);
        scroll.setBounds(260, 100, 360, 180);
        form.add(scroll);

        JButton submit = createButton("Submit Complaint");
        submit.setBounds(260, 310, 360, 35);
        submit.addActionListener(e -> submitComplaint());
        form.add(submit);

        return form;
    }

    private JPanel createAdminTable() {

        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        String[] columns = {
                "Complaint ID",
                "Customer ID",
                "Type",
                "Message",
                "Status",
                "Created At"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        complaintsTable = new JTable(tableModel);
        complaintsTable.setRowHeight(70);
        complaintsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        complaintsTable.setForeground(darkText);
        complaintsTable.setBackground(Color.WHITE);
        complaintsTable.setSelectionBackground(new Color(240, 225, 235));
        complaintsTable.setShowGrid(true);
        complaintsTable.setGridColor(new Color(220, 210, 215));

        complaintsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        complaintsTable.getTableHeader().setBackground(tableHeader);
        complaintsTable.getTableHeader().setForeground(darkText);
        complaintsTable.getTableHeader().setPreferredSize(new Dimension(0, 50));

        complaintsTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());

        JScrollPane scroll = new JScrollPane(complaintsTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 210, 220)));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        buttons.setOpaque(false);

        JButton solved = createButton("Mark as Solved");
        JButton refresh = createButton("🔁 Refresh");

        solved.addActionListener(e -> markComplaintSolved());
        refresh.addActionListener(e -> loadComplaints());

        buttons.add(refresh);
        buttons.add(solved);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(darkText);
        return label;
    }

    private JButton createButton(String text) {

        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25; borderWidth:0; focusWidth:0"
        );

        return btn;
    }

    private void submitComplaint() {

        String type = complaintTypeBox.getSelectedItem().toString();
        String message = messageArea.getText().trim();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please write your complaint.");
            return;
        }

        int customerId = getCustomerIdByUsername(username);

        if (customerId == -1) {
            JOptionPane.showMessageDialog(this, "Customer ID not found.");
            return;
        }

        String sql =
                "INSERT INTO complaint(customer_id, complaint_type, message, status, created_at) " +
                        "VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setString(2, type);
            ps.setString(3, message);
            ps.setString(4, "Pending");

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Complaint submitted successfully!");
            messageArea.setText("");
            complaintTypeBox.setSelectedIndex(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Submit Error: " + e.getMessage());
        }
    }

    private int getCustomerIdByUsername(String username) {

        String sql = "SELECT customer_id FROM users WHERE username = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("customer_id");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Customer Error: " + e.getMessage());
        }

        return -1;
    }

    private void loadComplaints() {

        if (tableModel == null) return;

        tableModel.setRowCount(0);

        String sql =
                "SELECT complaint_id, customer_id, complaint_type, message, status, created_at " +
                        "FROM complaint ORDER BY created_at DESC";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("complaint_id"),
                        rs.getInt("customer_id"),
                        rs.getString("complaint_type"),
                        rs.getString("message"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    private void markComplaintSolved() {

        int row = complaintsTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a complaint first.");
            return;
        }

        int complaintId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        String sql = "UPDATE complaint SET status = 'Solved' WHERE complaint_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, complaintId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Complaint marked as solved.");
            loadComplaints();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
        }
    }

    class StatusRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
        ) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setHorizontalAlignment(JLabel.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));

            if (value != null && value.toString().equalsIgnoreCase("Solved")) {
                c.setForeground(new Color(46, 130, 80));
            } else {
                c.setForeground(new Color(100, 100, 100));
            }

            c.setBackground(isSelected ? new Color(240, 225, 235) : Color.WHITE);
            return c;
        }
    }

    class RoundedPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(bgColor);
            g2.fillRect(0, 0, w, h);

            g2.setColor(lightCard);
            g2.fillRoundRect(0, 0, w, h - 70, 30, 30);

            g2.setColor(new Color(250, 235, 220));
            g2.fillOval(-160, -80, 520, 640);

            g2.setColor(new Color(190, 105, 100));
            Polygon wave = new Polygon();
            wave.addPoint(0, h - 130);
            wave.addPoint(180, h - 165);
            wave.addPoint(380, h - 120);
            wave.addPoint(580, h - 150);
            wave.addPoint(800, h - 105);
            wave.addPoint(w, h - 155);
            wave.addPoint(w, h);
            wave.addPoint(0, h);
            g2.fillPolygon(wave);

            g2.dispose();
        }
    }
}