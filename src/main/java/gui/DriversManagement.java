package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DriversManagement extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color cardColor = new Color(245, 210, 205);

    private JTable table;
    private DefaultTableModel model;
    private JFrame parentFrame;

    public DriversManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Delivery Company - Drivers Management");

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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Drivers Fleet Management", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(darkText);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Salary", "Location", "Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("✚ Add Driver");
        JButton btnUpdate = createStyledButton("🔁 Update Driver");
        JButton btnDelete = createStyledButton("❎ Remove Driver");
        JButton btnBack = createStyledButton("← Back to Dashboard");

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnUpdate);
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnBack);

        btnAdd.addActionListener(e -> openDriverDialog("Add New Driver", null));
        
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Object[] rowData = {
                    model.getValueAt(row, 0),
                    model.getValueAt(row, 1),
                    model.getValueAt(row, 2),
                    model.getValueAt(row, 3),
                    model.getValueAt(row, 4)
                };
                openDriverDialog("Update Driver", rowData);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a driver from the table!");
            }
        });

        btnDelete.addActionListener(e -> deleteDriver());

        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadDrivers();
    }

    private void styleTable() {
        table.setRowHeight(38);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setBackground(buttonColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        table.setSelectionBackground(cardColor);
        table.setSelectionForeground(darkText);
        table.setShowVerticalLines(false);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:20; focusWidth:0");
        return btn;
    }

    private void loadDrivers() {
        model.setRowCount(0);
        try (Connection conn = DataBaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT driver_id, name, salary, location, phone FROM driver")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getString(5)});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void openDriverDialog(String title, Object[] data) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 600); // زدنا الطول قليلاً ليستوعب الحقل الجديد
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(235, 185, 175));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(5, 0, 5, 0);

        // حقل الـ ID
        JTextField idField = createField(data != null ? data[0].toString() : "");
        if (data != null) {
            idField.setEditable(false); // لا يمكن تعديل الـ ID عند التحديث
            idField.setEnabled(false);
        }

        JTextField nameField = createField(data != null ? data[1].toString() : "");
        JTextField salaryField = createField(data != null ? data[2].toString() : "");
        JTextField locationField = createField(data != null ? data[3].toString() : "");
        JTextField phoneField = createField(data != null ? data[4].toString() : "");

        panel.add(new JLabel("Driver ID:"), gbc);
        panel.add(idField, gbc);
        panel.add(new JLabel("Full Name:"), gbc);
        panel.add(nameField, gbc);
        panel.add(new JLabel("Salary:"), gbc);
        panel.add(salaryField, gbc);
        panel.add(new JLabel("Location:"), gbc);
        panel.add(locationField, gbc);
        panel.add(new JLabel("Phone:"), gbc);
        panel.add(phoneField, gbc);

        JButton btnSave = createStyledButton("SAVE CHANGES");
        btnSave.addActionListener(e -> {
            try {
                String idText = idField.getText().trim();
                String name = nameField.getText().trim();
                String salaryText = salaryField.getText().trim();
                String loc = locationField.getText().trim();
                String phone = phoneField.getText().trim();

                if (idText.isEmpty() || name.isEmpty() || salaryText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill ID, Name and Salary!");
                    return;
                }

                int id = Integer.parseInt(idText);
                double salary = Double.parseDouble(salaryText);

                try (Connection conn = DataBaseConnection.getConnection()) {
                    String sql;
                    if (data == null) {
                        sql = "INSERT INTO driver (driver_id, name, salary, location, phone) VALUES (?, ?, ?, ?, ?)";
                    } else {
                        sql = "UPDATE driver SET name=?, salary=?, location=?, phone=? WHERE driver_id=?";
                    }

                    PreparedStatement ps = conn.prepareStatement(sql);
                    
                    if (data == null) {
                        ps.setInt(1, id);
                        ps.setString(2, name);
                        ps.setDouble(3, salary);
                        ps.setString(4, loc);
                        ps.setString(5, phone);
                    } else {
                        ps.setString(1, name);
                        ps.setDouble(2, salary);
                        ps.setString(3, loc);
                        ps.setString(4, phone);
                        ps.setInt(5, id);
                    }

                    ps.executeUpdate();
                    loadDrivers();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Success!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "ID and Salary must be numbers!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database Error: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(Box.createVerticalStrut(15), gbc);
        panel.add(btnSave, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JTextField createField(String text) {
        JTextField field = new JTextField(text);
        field.putClientProperty(FlatClientProperties.STYLE, "arc:15");
        field.setPreferredSize(new Dimension(200, 38));
        return field;
    }

    private void deleteDriver() {
int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a driver first.");
        return;
    }

int driverId = Integer.parseInt(model.getValueAt(row, 0).toString());
    try (Connection conn = DataBaseConnection.getConnection()) {

        PreparedStatement checkOrders = conn.prepareStatement(
                "SELECT COUNT(*) FROM orders WHERE driver_id = ?"
        );
        checkOrders.setInt(1, driverId);
        ResultSet rsOrders = checkOrders.executeQuery();
        rsOrders.next();

        PreparedStatement checkVehicle = conn.prepareStatement(
                "SELECT COUNT(*) FROM vehicle WHERE driver_id = ?"
        );
        checkVehicle.setInt(1, driverId);
        ResultSet rsVehicle = checkVehicle.executeQuery();
        rsVehicle.next();

        if (rsOrders.getInt(1) > 0 || rsVehicle.getInt(1) > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cannot delete this driver because he is linked to orders or vehicles."
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this driver?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        PreparedStatement deleteStmt = conn.prepareStatement(
                "DELETE FROM driver WHERE driver_id = ?"
        );
        deleteStmt.setInt(1, driverId);
        deleteStmt.executeUpdate();

model.removeRow(row);
        JOptionPane.showMessageDialog(this, "Driver deleted successfully.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
    }
}
}