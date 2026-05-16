package dao;

import java.sql.*;
import database.DataBaseConnection;

public class PaymentDAO {

    public void insert(int payment_id, String payment_way,
                       double required_price, double price_paid,
                       double rest_of_mount, int invoice_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Payment VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, payment_id);
        ps.setString(2, payment_way);
        ps.setDouble(3, required_price);
        ps.setDouble(4, price_paid);
        ps.setDouble(5, rest_of_mount);
        ps.setInt(6, invoice_id);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Payment");
    }
}