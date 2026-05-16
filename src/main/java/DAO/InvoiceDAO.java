package dao;

import java.sql.*;
import database.DataBaseConnection;

public class InvoiceDAO {

    public void insert(int invoice_id, String date, double total_price,
                       int order_id, double discount) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Invoice VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, invoice_id);
        ps.setString(2, date);
        ps.setDouble(3, total_price);
        ps.setInt(4, order_id);
        ps.setDouble(5, discount);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Invoice");
    }

    public void delete(int id) throws Exception {
        PreparedStatement ps = DataBaseConnection.getConnection()
                .prepareStatement("DELETE FROM Invoice WHERE invoice_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}