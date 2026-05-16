package gui;

import database.DataBaseConnection;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvoicesManagement extends JFrame {

    private final Color bgColor = new Color(214, 156, 148);
    private final Color darkText = new Color(58, 32, 42);
    private final Color buttonColor = new Color(160, 92, 148);
    private final Color cardColor = new Color(245, 210, 205);
    private final Color invoicePaper = new Color(255, 248, 245);

    private JTable invoiceTable;
    private DefaultTableModel tableModel;

    private String role;
    private String username;
    private JFrame parentFrame;

    public InvoicesManagement(String role, String username, JFrame parentFrame) {

        this.role = role;
        this.username = username;
        this.parentFrame = parentFrame;

        setTitle("Door2Door - Invoices Management");

        try {
            ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/delivery_logo.png"));
            Image appImg = appIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            setIconImage(appImg);
        } catch (Exception e) {
        }

        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel(
                role.equalsIgnoreCase("Customer") ? "My Invoices" : "Invoices Management",
                SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(darkText);

        String[] columns = {
                "Invoice ID", "Payment ID", "Customer ID", "Payment Way",
                "Required Price", "Paid Amount", "Remaining", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        invoiceTable = new JTable(tableModel);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:25; borderWidth:0; focusWidth:0"
        );

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setOpaque(false);

        JButton btnView = createButton("View Invoice");
        JButton btnRefresh = createButton("Refresh");
        JButton btnBack = createButton("Back to Dashboard");

        bottomPanel.add(btnView);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);

        btnView.addActionListener(e -> viewInvoice());
        btnRefresh.addActionListener(e -> loadInvoices());

        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            dispose();
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parentFrame != null) parentFrame.setVisible(true);
            }
        });

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadInvoices();
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

    private void styleTable() {
        invoiceTable.setRowHeight(38);
        invoiceTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        invoiceTable.setForeground(darkText);
        invoiceTable.setBackground(Color.WHITE);
        invoiceTable.setGridColor(cardColor);
        invoiceTable.setSelectionBackground(cardColor);
        invoiceTable.setSelectionForeground(darkText);
        invoiceTable.setShowVerticalLines(false);
        invoiceTable.setFillsViewportHeight(true);

        invoiceTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        invoiceTable.getTableHeader().setBackground(buttonColor);
        invoiceTable.getTableHeader().setForeground(Color.WHITE);

        invoiceTable.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());
    }

    private void loadInvoices() {
        tableModel.setRowCount(0);

        String sql;

        if (role.equalsIgnoreCase("Customer")) {
            sql =
                    "SELECT p.invoice_id, p.payment_id, p.customer_id, p.payment_way, " +
                            "p.required_price, p.price_paid, p.rest_of_mount " +
                            "FROM payment p " +
                            "JOIN users u ON p.customer_id = u.customer_id " +
                            "WHERE u.username = ?";
        } else {
            sql =
                    "SELECT invoice_id, payment_id, customer_id, payment_way, " +
                            "required_price, price_paid, rest_of_mount " +
                            "FROM payment";
        }

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (role.equalsIgnoreCase("Customer")) {
                ps.setString(1, username);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double rest = rs.getDouble("rest_of_mount");

                tableModel.addRow(new Object[]{
                        rs.getInt("invoice_id"),
                        rs.getInt("payment_id"),
                        rs.getInt("customer_id"),
                        rs.getString("payment_way"),
                        rs.getDouble("required_price"),
                        rs.getDouble("price_paid"),
                        rest,
                        rest <= 0 ? "FULLY PAID" : "PENDING"
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    private void viewInvoice() {

        int row = invoiceTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an invoice first.");
            return;
        }

        String invoiceId = tableModel.getValueAt(row, 0).toString();
        String paymentId = tableModel.getValueAt(row, 1).toString();
        String customerId = tableModel.getValueAt(row, 2).toString();
        String paymentWay = tableModel.getValueAt(row, 3).toString();
        String requiredPrice = tableModel.getValueAt(row, 4).toString();
        String paidAmount = tableModel.getValueAt(row, 5).toString();
        String status = tableModel.getValueAt(row, 7).toString();

        this.setVisible(false);

        JDialog dialog = new JDialog(this, "Invoice", true);
        dialog.setSize(720, 790);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(232, 218, 212));
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel invoicePanel = new JPanel() {

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

                g2.setColor(invoicePaper);
                g2.fillRoundRect(0, 0, w, h, 12, 12);

                g2.setColor(buttonColor);
                g2.fillRect(20, 80, 290, 55);
                g2.setColor(buttonColor);
                g2.setStroke(new BasicStroke(4));
                g2.drawLine(415, 80, 415, 165);
                g2.setColor(darkText);
                g2.setStroke(new BasicStroke(3));

                drawLineWithDots(g2, 55, 215, w - 55, 215);
                drawLineWithDots(g2, 55, 255, w - 55, 255);
                drawLineWithDots(g2, 55, 455, w - 55, 455);
                drawLineWithDots(g2, 55, 495, w - 55, 495);
                drawLineWithDots(g2, 45, h - 35, 350, h - 35);
                drawLineWithDots(g2, 560, h - 35, w - 45, h - 35);

                g2.dispose();
            }
        };

        invoicePanel.setLayout(null);
        invoicePanel.setBackground(invoicePaper);

        String date = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(new Date());

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Monospaced", Font.BOLD, 17));
        dateLabel.setForeground(darkText);
        dateLabel.setBounds(45, 25, 230, 28);
        invoicePanel.add(dateLabel);

        JLabel invoiceTitle = new JLabel("INVOICE");
        invoiceTitle.setFont(new Font("Serif", Font.BOLD, 36));
        invoiceTitle.setForeground(Color.WHITE);
        invoiceTitle.setBounds(75, 82, 250, 45);
        invoicePanel.add(invoiceTitle);

        JLabel invoiceNo = new JLabel("INVOICE NO   # " + invoiceId);
        invoiceNo.setFont(new Font("SansSerif", Font.BOLD, 21));
        invoiceNo.setForeground(darkText);
        invoiceNo.setBounds(65, 155, 300, 30);
        invoicePanel.add(invoiceNo);

        JLabel invoiceTo = new JLabel(
                "<html><b>INVOICE TO:</b><br>" +
                        username + "<br>" +
                        "Customer ID: " + customerId + "<br>" +
                        "door2door@email.com</html>"
        );
        invoiceTo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        invoiceTo.setForeground(darkText);
invoiceTo.setBounds(445, 80, 180, 90);
invoicePanel.add(invoiceTo);

        addHeader(invoicePanel, "DESCRIPTION", 80, 220, 130, 22);
addHeader(invoicePanel, "PRICE", 275, 220, 70, 22);
addHeader(invoicePanel, "QTY", 395, 220, 50, 22);
addHeader(invoicePanel, "TOTAL", 505, 220, 70, 22);

        addInvoiceRow(invoicePanel, 290, "Delivery Service", "$" + requiredPrice, "1", "$" + paidAmount);
        addInvoiceRow(invoicePanel, 335, "Payment Way: " + paymentWay, "$0.00", "1", "$0.00");
        addInvoiceRow(invoicePanel, 380, "Payment ID: " + paymentId, "$0.00", "1", "$0.00");
        addInvoiceRow(invoicePanel, 425, "Status: " + status, "$0.00", "1", "$0.00");

        JLabel totalText = new JLabel("TOTAL");
        totalText.setFont(new Font("Serif", Font.BOLD, 29));
        totalText.setForeground(darkText);
        totalText.setBounds(90, 460, 120, 30);
        invoicePanel.add(totalText);

        JLabel totalAmount = new JLabel("$" + paidAmount, SwingConstants.RIGHT);
        totalAmount.setFont(new Font("Monospaced", Font.BOLD, 28));
        totalAmount.setForeground(darkText);
totalAmount.setBounds(485, 460, 120, 30);
invoicePanel.add(totalAmount);

        JLabel bank = new JLabel(
                "<html><b>BANK DETAILS</b><br>" +
                        "Bank Name<br>" +
                        "Account Name: Door2Door<br>" +
                        "Account No: XXXXXXXX</html>"
        );
        bank.setFont(new Font("Monospaced", Font.PLAIN, 13));
        bank.setForeground(darkText);
       bank.setBounds(70, 525, 220, 65);
        invoicePanel.add(bank);

        JLabel summary = new JLabel(
                "<html>" +
                        "Total&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $" + paidAmount + "<br>" +
                        "<b>Tax</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (0)%" +
                        "</html>"
        );
        summary.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summary.setForeground(darkText);
summary.setBounds(390, 525, 170, 55);
invoicePanel.add(summary);

        JLabel grandTotal = new JLabel("Grand Total      $" + paidAmount, SwingConstants.CENTER);
        grandTotal.setOpaque(true);
        grandTotal.setBackground(buttonColor);
        grandTotal.setForeground(Color.WHITE);
        grandTotal.setFont(new Font("Serif", Font.BOLD, 21));
grandTotal.setBounds(340, 615, 250, 34);
invoicePanel.add(grandTotal);

        JLabel thanks = new JLabel("Thank You.");
        thanks.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        thanks.setForeground(darkText);
thanks.setBounds(370, 665, 220, 45);
invoicePanel.add(thanks);

        JButton okBtn = createButton("OK");
        okBtn.setBounds(195, 675, 110, 34);
        invoicePanel.add(okBtn);

        okBtn.addActionListener(e -> {
            dialog.dispose();
            this.setVisible(true);
        });

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                InvoicesManagement.this.setVisible(true);
            }
        });

        wrapper.add(invoicePanel, BorderLayout.CENTER);
        dialog.add(wrapper);
        dialog.setVisible(true);
    }

    private void drawLineWithDots(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.drawLine(x1, y1, x2, y2);
        g2.fillOval(x1 - 5, y1 - 5, 10, 10);
        g2.fillOval(x2 - 5, y2 - 5, 10, 10);
    }

    private void addHeader(JPanel panel, String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 19));
        label.setForeground(darkText);
        label.setBounds(x, y, width, height);
        panel.add(label);
    }

    private void addInvoiceRow(JPanel panel, int y, String desc, String price, String qty, String total) {

        JLabel d = new JLabel(desc);
        d.setFont(new Font("Monospaced", Font.PLAIN, 16));
        d.setForeground(darkText);
        d.setBounds(75, y, 180, 22);
        panel.add(d);

        JLabel p = new JLabel(price, SwingConstants.CENTER);
        p.setFont(new Font("Monospaced", Font.BOLD, 16));
        p.setForeground(darkText);
        p.setBounds(275, y, 80, 22);
        panel.add(p);

        JLabel q = new JLabel(qty, SwingConstants.CENTER);
        q.setFont(new Font("Monospaced", Font.PLAIN, 16));
        q.setForeground(darkText);
        q.setBounds(395, y, 50, 22);
        panel.add(q);

        JLabel t = new JLabel(total, SwingConstants.CENTER);
        t.setFont(new Font("Monospaced", Font.BOLD, 16));
        t.setForeground(darkText);
        t.setBounds(500, y, 80, 22);
        panel.add(t);
    }

    class StatusRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            Component c = super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setHorizontalAlignment(JLabel.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));

            if (value != null && value.toString().equalsIgnoreCase("FULLY PAID")) {
                c.setForeground(new Color(46, 130, 80));
            } else {
                c.setForeground(new Color(150, 40, 40));
            }

            c.setBackground(isSelected ? cardColor : Color.WHITE);

            return c;
        }
    }
}