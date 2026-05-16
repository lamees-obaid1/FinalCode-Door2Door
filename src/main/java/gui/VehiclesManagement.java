package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VehiclesManagement extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color cardColor = new Color(245, 210, 205);

    private JTable table;
    private DefaultTableModel model;
    private JFrame parentFrame;

    public VehiclesManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Delivery Company - Vehicles Management");

        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/delivery_logo.png"));
            Image appImg = appIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(appImg);
        } catch (Exception e) {}

        setSize(1050, 650);
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

        JLabel title = new JLabel("Vehicles Management", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(darkText);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        model.setColumnIdentifiers(new String[]{
                "Vehicle ID", "Type", "Model", "Year", "Position", "Driver No", "Driver ID"
        });

        table = new JTable(model);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, "arc:25; borderWidth:0; focusWidth:0");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(bgColor);

        JButton addBtn = createButton("✚ Add New Vehicle");
        JButton updateBtn = createButton("🔁 Update Selected");
        JButton deleteBtn = createButton("❎ Delete Vehicle");
        JButton backBtn = createButton("← Back");

        buttonsPanel.add(addBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(backBtn);

        addBtn.addActionListener(e -> addVehicle());
        updateBtn.addActionListener(e -> updateVehicle());
        deleteBtn.addActionListener(e -> deleteVehicle());
        backBtn.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadVehicles();
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

    private JButton createButton(String text) {
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

    private void loadVehicles() {
        model.setRowCount(0);
        String sql = "SELECT vehicle_id, type_of_car, model, year_of_issue, car_position, driver_number, driver_id FROM vehicle";
        try (Connection conn = DataBaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("vehicle_id"), rs.getString("type_of_car"), rs.getString("model"),
                        rs.getInt("year_of_issue"), rs.getString("car_position"), rs.getString("driver_number"),
                        rs.getInt("driver_id")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    private void addVehicle() {
        JPanel p = new JPanel(new GridLayout(7, 2, 10, 10));
        p.setBackground(bgColor);
        JTextField id = createField("");
        JTextField type = createField("");
        JTextField modelField = createField("");
        JTextField year = createField("");
        JTextField position = createField("");
        JTextField driverNo = createField("");
        JTextField driverId = createField("");
        addLabelAndField(p, "Vehicle ID:", id);
        addLabelAndField(p, "Type:", type);
        addLabelAndField(p, "Model:", modelField);
        addLabelAndField(p, "Year:", year);
        addLabelAndField(p, "Position:", position);
        addLabelAndField(p, "Driver No:", driverNo);
        addLabelAndField(p, "Driver ID:", driverId);

        int result = JOptionPane.showConfirmDialog(this, p, "Add New Vehicle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO vehicle(vehicle_id, type_of_car, model, year_of_issue, car_position, driver_number, driver_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = DataBaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(id.getText().trim()));
                    ps.setString(2, type.getText().trim());
                    ps.setString(3, modelField.getText().trim());
                    ps.setInt(4, Integer.parseInt(year.getText().trim()));
                    ps.setString(5, position.getText().trim());
                    ps.setString(6, driverNo.getText().trim());
                    ps.setInt(7, Integer.parseInt(driverId.getText().trim()));
                    ps.executeUpdate();
                }
                loadVehicles();
                JOptionPane.showMessageDialog(this, "Vehicle added successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Insert Error: " + e.getMessage());
            }
        }
    }

    private void updateVehicle() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle first.");
            return;
        }
        String vehicleId = model.getValueAt(row, 0).toString();
        JTextField type = createField(model.getValueAt(row, 1).toString());
        JTextField modelField = createField(model.getValueAt(row, 2).toString());
        JTextField year = createField(model.getValueAt(row, 3).toString());
        JTextField position = createField(model.getValueAt(row, 4).toString());
        JTextField driverNo = createField(model.getValueAt(row, 5).toString());
        JTextField driverId = createField(model.getValueAt(row, 6).toString());

        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        p.setBackground(bgColor);
        addLabelAndField(p, "Type:", type);
        addLabelAndField(p, "Model:", modelField);
        addLabelAndField(p, "Year:", year);
        addLabelAndField(p, "Position:", position);
        addLabelAndField(p, "Driver No:", driverNo);
        addLabelAndField(p, "Driver ID:", driverId);

        int result = JOptionPane.showConfirmDialog(this, p, "Update Vehicle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE vehicle SET type_of_car=?, model=?, year_of_issue=?, car_position=?, driver_number=?, driver_id=? WHERE vehicle_id=?";
                try (Connection conn = DataBaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, type.getText().trim());
                    ps.setString(2, modelField.getText().trim());
                    ps.setInt(3, Integer.parseInt(year.getText().trim()));
                    ps.setString(4, position.getText().trim());
                    ps.setString(5, driverNo.getText().trim());
                    ps.setInt(6, Integer.parseInt(driverId.getText().trim()));
                    ps.setInt(7, Integer.parseInt(vehicleId));
                    ps.executeUpdate();
                }
                loadVehicles();
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
            }
        }
    }

    private void deleteVehicle() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this vehicle?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int vehicleId = Integer.parseInt(model.getValueAt(row, 0).toString());
                String sql = "DELETE FROM vehicle WHERE vehicle_id=?";
                try (Connection conn = DataBaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, vehicleId);
                    ps.executeUpdate();
                }
                loadVehicles();
                JOptionPane.showMessageDialog(this, "Vehicle deleted successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
            }
        }
    }

    private void addLabelAndField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(darkText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(label);
        panel.add(field);
    }
}