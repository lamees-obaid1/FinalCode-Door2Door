package dao;

import java.sql.*;
import database.DataBaseConnection;

public class ComplaintDAO {

    public void insert(int Complaint_id, double required_price,
                       double price_paid, double rest_of_mount,
                       int invoice_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Complaint VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, Complaint_id);
        ps.setDouble(2, required_price);
        ps.setDouble(3, price_paid);
        ps.setDouble(4, rest_of_mount);
        ps.setInt(5, invoice_id);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Complaint");
    }
}