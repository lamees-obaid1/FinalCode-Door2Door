package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomersManagement extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JFrame parentFrame;

    // الهوية البصرية لشركة Door2Door
    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color cardColor = new Color(245, 210, 205);

    private String userRole;

    public CustomersManagement(String role, JFrame parentFrame) {
        this.userRole = role;
        this.parentFrame = parentFrame;

        setTitle("Door2Door - Customers Management [" + role + "]");

        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/delivery_logo.png"));
            Image appImg = appIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(appImg);
        } catch (Exception e) {}

        setSize(1000, 700);
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

        JLabel title = new JLabel("Customers Management", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(darkText);
        mainPanel.add(title, BorderLayout.NORTH);

        // موديل الجدول
        model = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Address", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomPanel.setBackground(bgColor);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonsPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("✚ Add New Customer");
        JButton btnUpdate = createStyledButton("🔁 Update Selected");
        JButton btnDelete = createStyledButton("❎ Delete Customer");
        JButton btnBack = createStyledButton("← Back to Dashboard");

        // التحكم بالصلاحيات
        if (userRole != null && (userRole.equalsIgnoreCase("Admin") || userRole.equalsIgnoreCase("Customer Service"))) {
            buttonsPanel.add(btnAdd);
            buttonsPanel.add(btnUpdate);
            if (userRole.equalsIgnoreCase("Admin")) {
                buttonsPanel.add(btnDelete);
            }
        } else {
            JLabel readOnlyLabel = new JLabel("View Only Mode - Restricted Access");
            readOnlyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            readOnlyLabel.setForeground(darkText);
            buttonsPanel.add(readOnlyLabel);
        }

        bottomPanel.add(buttonsPanel);
        bottomPanel.add(btnBack);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        btnAdd.addActionListener(e -> openAddDialog());
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Please select a customer!"); return; }
            openUpdateDialog(row);
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Please select a customer!"); return; }
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) deleteFromDatabase(id);
        });

        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        add(mainPanel);
        loadData();
    }

    private void styleTable() {
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setForeground(darkText);
        table.setBackground(Color.WHITE);
        table.setGridColor(cardColor);
        table.setSelectionBackground(cardColor);
        table.setSelectionForeground(darkText);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(buttonColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");
        return btn;
    }

    private JTextField createField(String text) {
        JTextField field = new JTextField(text);
        field.setForeground(darkText);
        field.putClientProperty(FlatClientProperties.STYLE, "arc:20; focusColor:#522536; borderColor:#994853");
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(darkText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT ID, Name, phone, Address, Email FROM customers";
        try (Connection conn = DataBaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("ID"), rs.getString("Name"), rs.getString("phone"), rs.getString("Address"), rs.getString("Email")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    private void openAddDialog() { showCustomerDialog("Add New Customer", null); }

    private void openUpdateDialog(int row) {
        Object[] data = {model.getValueAt(row, 0), model.getValueAt(row, 1), model.getValueAt(row, 2), model.getValueAt(row, 3), model.getValueAt(row, 4)};
        showCustomerDialog("Update Customer", data);
    }

    // النافذة المنبثقة المعدلة لتشمل حقل الـ ID
    private void showCustomerDialog(String title, Object[] data) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(430, 580);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // تعريف الحقول
        JTextField idField = createField(data != null ? data[0].toString() : "");
        JTextField nameField = createField(data != null ? data[1].toString() : "");
        JTextField phoneField = createField(data != null ? data[2].toString() : "");
        JTextField addressField = createField(data != null ? data[3].toString() : "");
        JTextField emailField = createField(data != null ? data[4].toString() : "");

        // في حالة التحديث، نغلق تعديل الـ ID
        if (data != null) {
            idField.setEditable(false);
            idField.setFocusable(false);
            idField.setBackground(cardColor);
        }

        JButton saveBtn = createStyledButton(data == null ? "SAVE CUSTOMER" : "UPDATE CUSTOMER");

        // إضافة العناصر للوحة
        addRow(panel, gbc, new JLabel(title) {{ setFont(new Font("SansSerif", Font.BOLD, 20)); setForeground(darkText); }}, 0, 0, 20, 0);
        
        addRow(panel, gbc, createLabel("Customer ID"), 0, 0, 5, 0);
        addRow(panel, gbc, idField, 0, 0, 10, 0);
        
        addRow(panel, gbc, createLabel("Full Name"), 0, 0, 5, 0);
        addRow(panel, gbc, nameField, 0, 0, 10, 0);
        
        addRow(panel, gbc, createLabel("Phone Number"), 0, 0, 5, 0);
        addRow(panel, gbc, phoneField, 0, 0, 10, 0);
        
        addRow(panel, gbc, createLabel("Address"), 0, 0, 5, 0);
        addRow(panel, gbc, addressField, 0, 0, 10, 0);
        
        addRow(panel, gbc, createLabel("Email"), 0, 0, 5, 0);
        addRow(panel, gbc, emailField, 0, 0, 25, 0);
        
        addRow(panel, gbc, saveBtn, 0, 0, 10, 0);

        saveBtn.addActionListener(e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill ID and Name!");
                return;
            }
            try {
                int id = Integer.parseInt(idField.getText());
                if (data == null) {
                    saveToDatabase(id, nameField.getText(), phoneField.getText(), addressField.getText(), emailField.getText());
                } else {
                    updateInDatabase(id, nameField.getText(), phoneField.getText(), addressField.getText(), emailField.getText());
                }
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "ID must be a number!");
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, Component component, int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
        panel.add(component, gbc);
    }

    private void saveToDatabase(int id, String name, String phone, String address, String email) {
        String sql = "INSERT INTO customers (ID, Name, phone, Address, Email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name); 
            pstmt.setString(3, phone); 
            pstmt.setString(4, address); 
            pstmt.setString(5, email);
            pstmt.executeUpdate(); 
            loadData();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage()); }
    }

    private void updateInDatabase(int id, String name, String phone, String address, String email) {
        String sql = "UPDATE customers SET Name=?, phone=?, Address=?, Email=? WHERE ID=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name); 
            pstmt.setString(2, phone); 
            pstmt.setString(3, address); 
            pstmt.setString(4, email); 
            pstmt.setInt(5, id);
            pstmt.executeUpdate(); 
            loadData();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage()); }
    }

   private void deleteFromDatabase(int id) {

    try (Connection conn = DataBaseConnection.getConnection()) {

        PreparedStatement deleteAddress = conn.prepareStatement(
                "DELETE FROM address WHERE customer_number = ?"
        );

        deleteAddress.setInt(1, id);
        deleteAddress.executeUpdate();

        PreparedStatement deleteCustomer = conn.prepareStatement(
                "DELETE FROM customers WHERE ID = ?"
        );

        deleteCustomer.setInt(1, id);
        deleteCustomer.executeUpdate();

        loadData();

        JOptionPane.showMessageDialog(
                this,
                "Customer deleted successfully!"
        );

    } catch (SQLException e) {

    JOptionPane.showMessageDialog(
            this,
            "Cannot delete this customer because they are linked to other records in the system."
    );
}
}
}