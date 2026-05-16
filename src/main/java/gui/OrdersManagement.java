package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Locale;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrdersManagement extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color fieldColor = new Color(235, 185, 175);
    private final Color cardColor = new Color(245, 210, 205);

    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JFrame parentFrame;
    private String userRole;
    private String username;

    public OrdersManagement(String role, String username, JFrame parentFrame) {
        this.userRole = role;
        this.username = username;
        this.parentFrame = parentFrame;
        
        setTitle("Door2Door - Orders Management [" + role + "]");

        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/delivery_logo.png"));
            Image appImg = appIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(appImg);
        } catch (Exception e) {}

        setSize(1100, 700);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(role.equalsIgnoreCase("Customer") ? "My Orders History" : "Orders Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(darkText);

        JButton btnAddOrder = createButton("Add New Order +");
        btnAddOrder.addActionListener(e -> openOrderDialog());
        
       
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnAddOrder, BorderLayout.EAST);

        String[] columns = {"ID", "Date", "Address", "Price", "Status", "Customer ID", "Driver ID", "Branch ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        ordersTable = new JTable(tableModel);
        styleTable();
        if (!role.equalsIgnoreCase("Customer")) {
    setupTablePopupMenu();
}

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);

        JButton btnBack = createButton("← Back to Dashboard");
        btnBack.setPreferredSize(new Dimension(220, 45));
        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        footerPanel.add(btnBack);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        fetchOrdersFromDB();
    }

    private void styleTable() {
        ordersTable.setRowHeight(38);
        ordersTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ordersTable.setForeground(darkText);
        ordersTable.setBackground(Color.WHITE);
        ordersTable.setGridColor(cardColor);
        ordersTable.setSelectionBackground(cardColor);
        ordersTable.setSelectionForeground(darkText);
        ordersTable.setShowVerticalLines(false);
        ordersTable.setFillsViewportHeight(true);
        ordersTable.getTableHeader().setBackground(buttonColor);
        ordersTable.getTableHeader().setForeground(Color.WHITE);
        ordersTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        ordersTable.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
    }

    private void setupTablePopupMenu() {

    JPopupMenu popupMenu = new JPopupMenu();

    JMenuItem compensateItem =
            new JMenuItem("Compensate with Free Delivery");

    compensateItem.addActionListener(e -> {

        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow != -1) {

            int customerId =
                    (int) ordersTable.getValueAt(selectedRow, 5);

            giveFreeDeliveryCompensation(customerId);
        }
    });

    popupMenu.add(compensateItem);

    ordersTable.addMouseListener(new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) showPopup(e);
        }

        private void showPopup(MouseEvent e) {

            int row = ordersTable.rowAtPoint(e.getPoint());

            if (row >= 0 && row < ordersTable.getRowCount()) {

                ordersTable.setRowSelectionInterval(row, row);

                popupMenu.show(
                        e.getComponent(),
                        e.getX(),
                        e.getY()
                );
            }
        }
    });
}

private void giveFreeDeliveryCompensation(int customerId) {

    String sql =
            "UPDATE customers SET next_delivery_free = TRUE WHERE ID = ?";

    try (
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
    ) {

        pstmt.setInt(1, customerId);

        int rows = pstmt.executeUpdate();

        if (rows > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Customer " + customerId +
                            " compensated with Free Delivery!"
            );
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Compensation Error: " + e.getMessage()
        );
    }
}

private double checkAndApplyCompensation(
        int customerId,
        double currentPrice
) {

    String checkSql =
            "SELECT next_delivery_free FROM customers WHERE ID = ?";

    String resetSql =
            "UPDATE customers SET next_delivery_free = FALSE WHERE ID = ?";

    try (
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement checkStmt =
                    conn.prepareStatement(checkSql)
    ) {

        checkStmt.setInt(1, customerId);

        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() &&
                rs.getBoolean("next_delivery_free")) {

            double discount = 5.0;

            double finalPrice =
                    Math.max(0, currentPrice - discount);

            try (
                    PreparedStatement resetStmt =
                            conn.prepareStatement(resetSql)
            ) {

                resetStmt.setInt(1, customerId);

                resetStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Compensation Applied: " +
                            "$5.00 Delivery Fee Waived!"
            );

            return finalPrice;
        }

    } catch (SQLException e) {

        System.out.println(
                "Compensation Logic Error: " + e.getMessage()
        );
    }

    return currentPrice;
}
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(darkText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setForeground(darkText);
        field.setCaretColor(darkText);
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        field.putClientProperty(FlatClientProperties.STYLE, "arc:20; focusColor:#522536; borderColor:#994853");
        return field;
    }

    private void openOrderDialog() {
        JDialog dialog = new JDialog(this, "Add New Order", true);
        dialog.setSize(480, 720);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel title = new JLabel("Add New Order", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(darkText);

        JTextField txtOrderId = createField("Enter order ID");
        JTextField txtAddress = createField("Enter delivery address");
        JTextField txtPrice = createField("Enter total price");
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{"Pending", "Shipped", "Delivered"});
        comboStatus.setBackground(fieldColor);

        JTextField txtCustId = createField("Enter customer ID");
        JTextField txtDriverId = createField("Enter driver ID");
        JTextField txtBranchId = createField("Enter branch ID");

        JTextField txtCoupon = createField("Enter coupon code");
        JLabel lblDiscountStatus = new JLabel("No discount applied", SwingConstants.LEFT);
        lblDiscountStatus.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblDiscountStatus.setForeground(darkText);
        
        JButton btnApply = new JButton("Apply");
        btnApply.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApply.addActionListener(e -> {

    String code = txtCoupon.getText().trim();

    try {

        double price = Double.parseDouble(txtPrice.getText().trim());

        if (code.equalsIgnoreCase("SAVE10")) {

            double newPrice = price * 0.90;

            txtPrice.setText(String.format(java.util.Locale.US, "%.2f", newPrice));

            lblDiscountStatus.setText("Coupon applied! 10% Discounted.");
            lblDiscountStatus.setForeground(new Color(46,130,80));

            btnApply.setEnabled(false);

        }

        else {

            JOptionPane.showMessageDialog(dialog,
                    "Invalid or expired coupon code.");

        }

    }

    catch (Exception ex) {

        JOptionPane.showMessageDialog(dialog,
                "Please enter valid price first.");

    }

});

        JButton btnSave = createButton("SAVE ORDER");
        btnSave.addActionListener(e -> {
            saveOrderToDB(txtOrderId.getText().trim(), txtAddress.getText().trim(), txtPrice.getText().trim(), comboStatus.getSelectedItem().toString(),
                          txtCustId.getText().trim(), txtDriverId.getText().trim(), txtBranchId.getText().trim());
            dialog.dispose();
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setForeground(darkText);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dialog.dispose());

        addRow(panel, gbc, title, 0, 0, 20, 0);
        addRow(panel, gbc, createLabel("Order ID"), 0, 0, 5, 0);
        addRow(panel, gbc, txtOrderId, 0, 0, 12, 0);
        addRow(panel, gbc, createLabel("Delivery Address"), 0, 0, 5, 0);
        addRow(panel, gbc, txtAddress, 0, 0, 12, 0);
        addRow(panel, gbc, createLabel("Total Price ($)"), 0, 0, 5, 0);
        addRow(panel, gbc, txtPrice, 0, 0, 12, 0);
        
        addRow(panel, gbc, createLabel("Coupon Code"), 0, 0, 5, 0);
        JPanel couponPanel = new JPanel(new BorderLayout(5, 0));
        couponPanel.setOpaque(false);
        couponPanel.add(txtCoupon, BorderLayout.CENTER);
        couponPanel.add(btnApply, BorderLayout.EAST);
        addRow(panel, gbc, couponPanel, 0, 0, 2, 0);
        addRow(panel, gbc, lblDiscountStatus, 0, 0, 12, 0);

        addRow(panel, gbc, createLabel("Order Status"), 0, 0, 5, 0);
        addRow(panel, gbc, comboStatus, 0, 0, 12, 0);
        addRow(panel, gbc, createLabel("Customer ID"), 0, 0, 5, 0);
        addRow(panel, gbc, txtCustId, 0, 0, 12, 0);
        addRow(panel, gbc, createLabel("Driver ID"), 0, 0, 5, 0);
        addRow(panel, gbc, txtDriverId, 0, 0, 12, 0);
        addRow(panel, gbc, createLabel("Branch ID"), 0, 0, 5, 0);
        addRow(panel, gbc, txtBranchId, 0, 0, 20, 0);
        addRow(panel, gbc, btnSave, 0, 0, 10, 0);
        addRow(panel, gbc, btnCancel, 0, 0, 0, 0);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, Component component, int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
        panel.add(component, gbc);
    }

    // --- الميثود الجديدة للبحث عن الكوبون في قاعدة البيانات ---
    private double checkCouponInDB(String code) {
        // افترضنا هنا أن الجدول اسمه coupons والعمود اسمه discount_value ونوع الكوبون اسمه coupon_code
        String sql = "SELECT discount_value FROM coupons WHERE coupon_code = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("discount_value"); // القيمة تكون مثلاً 0.10 لخصم 10%
            }
        } catch (SQLException e) {
            System.out.println("Coupon DB Error: " + e.getMessage());
        }
        return 0.0;
    }

private void saveOrderToDB(String orderId,
                           String address,
                           String price,
                           String status,
                           String cId,
                           String dId,
                           String bId){
    if (orderId.isEmpty() || address.isEmpty() || price.isEmpty() || cId.isEmpty() || dId.isEmpty() || bId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        String sql = "INSERT INTO orders (order_id, date_of_order, delivery_address, total_price, order_status, customer_id, driver_id, branch_id) VALUES (?, CURDATE(), ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
pstmt.setInt(1, Integer.parseInt(orderId));

pstmt.setString(2, address);

price = price.replace("٠","0")
             .replace("١","1")
             .replace("٢","2")
             .replace("٣","3")
             .replace("٤","4")
             .replace("٥","5")
             .replace("٦","6")
             .replace("٧","7")
             .replace("٨","8")
             .replace("٩","9")
             .replace("٫",".")
             .replace("،",".");

pstmt.setDouble(3, Double.parseDouble(price));

pstmt.setString(4, status);

int customerId = Integer.parseInt(cId);

double originalPrice = Double.parseDouble(price);

double finalPrice =
        checkAndApplyCompensation(
                customerId,
                originalPrice
        );

pstmt.setDouble(3, finalPrice);

pstmt.setInt(5, customerId);
pstmt.setInt(6, Integer.parseInt(dId));

pstmt.setInt(7, Integer.parseInt(bId));
            pstmt.executeUpdate();
            fetchOrdersFromDB();
            JOptionPane.showMessageDialog(this, "Order added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Insert Error: " + e.getMessage());
        }
    }

    private void fetchOrdersFromDB() {

    tableModel.setRowCount(0);

    String sql;

    boolean isCustomer =
            userRole.equalsIgnoreCase("Customer");

    if (isCustomer) {

        sql = """
              SELECT *
              FROM orders
              WHERE customer_id = 1
              """;

    } else {

        sql = "SELECT * FROM orders";
    }

    try (
            Connection conn =
                    DataBaseConnection.getConnection();

            PreparedStatement pstmt =
                    conn.prepareStatement(sql)
    ) {

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {

            tableModel.addRow(new Object[]{

                    rs.getInt("order_id"),
                    rs.getDate("date_of_order"),
                    rs.getString("delivery_address"),
                    rs.getBigDecimal("total_price"),
                    rs.getString("order_status"),
                    rs.getInt("customer_id"),
                    rs.getInt("driver_id"),
                    rs.getInt("branch_id")
            });
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Load Error: " + e.getMessage()
        );
    }
}

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = value != null ? value.toString() : "";
            setHorizontalAlignment(JLabel.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
            if ("Delivered".equalsIgnoreCase(status)) c.setForeground(new Color(46, 130, 80));
            else if ("Pending".equalsIgnoreCase(status)) c.setForeground(new Color(170, 90, 25));
            else if ("Shipped".equalsIgnoreCase(status)) c.setForeground(buttonColor);
            else c.setForeground(darkText);
            c.setBackground(isSelected ? cardColor : Color.WHITE);
            return c;
        }
    }
}