package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class OrderTracking extends JFrame {

    private JTextField txtId;
    private JTextArea txtRes;
    private JFrame parentFrame;

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color fieldColor = new Color(235, 185, 175);
    private final Color cardColor = new Color(245, 210, 205);

    public OrderTracking(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Delivery Company - Order Tracking");

        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/delivery_logo.png"));
            Image appImg = appIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(appImg);
        } catch (Exception e) {}

        setSize(520, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parentFrame != null) parentFrame.setVisible(true);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("Order Tracking", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(darkText);

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel searchLabel = new JLabel("Search by Order Number");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchLabel.setForeground(darkText);

        txtId = new JTextField();
        txtId.setForeground(darkText);
        txtId.setBackground(fieldColor);
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Order ID...");
        txtId.putClientProperty(FlatClientProperties.STYLE, "arc:25; height:42; focusColor:#522536; borderColor:#994853");

        JButton trackBtn = createButton("TRACK ORDER");
        trackBtn.addActionListener(e -> track());

        addRow(topPanel, gbc, title, 0, 0, 25, 0);
        addRow(topPanel, gbc, searchLabel, 0, 0, 6, 0);
        addRow(topPanel, gbc, txtId, 0, 0, 14, 0);
        addRow(topPanel, gbc, trackBtn, 0, 0, 0, 0);

        txtRes = new JTextArea();
        txtRes.setEditable(false);
        txtRes.setFont(new Font("Monospaced", Font.BOLD, 15));
        txtRes.setForeground(darkText);
        txtRes.setBackground(cardColor);
        txtRes.setLineWrap(true);
        txtRes.setWrapStyleWord(true);
        txtRes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        txtRes.setText("\nEnter an order ID above to view shipment status.");

        JScrollPane scrollPane = new JScrollPane(txtRes);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");

        JButton backBtn = new JButton("Cancel");
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setForeground(darkText);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(backBtn, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");
        return btn;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, Component component, int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
        panel.add(component, gbc);
    }

    private void track() {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Order ID.");
            return;
        }

        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "SELECT o.order_status, o.delivery_address, c.Name, d.name FROM orders o " +
                         "JOIN customers c ON o.customer_id = c.ID " +
                         "JOIN driver d ON o.driver_id = d.driver_id WHERE o.order_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idText));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtRes.setText("\n📦 Status: " + rs.getString(1).toUpperCase() +
                               "\n\n📍 Address: " + rs.getString(2) +
                               "\n\n👤 Customer: " + rs.getString(3) +
                               "\n\n🚚 Driver: " + rs.getString(4));
            } else {
                txtRes.setText("\nOrder ID not found!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Order ID must be a number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}