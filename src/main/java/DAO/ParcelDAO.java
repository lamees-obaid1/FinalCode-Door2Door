package dao;

import java.sql.*;
import database.DataBaseConnection;

public class ParcelDAO {

    public void insert(int parcel_id, double weight,
                       String description, int order_id,
                       int invoice, String comments) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Parcel VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, parcel_id);
        ps.setDouble(2, weight);
        ps.setString(3, description);
        ps.setInt(4, order_id);
        ps.setInt(5, invoice);
        ps.setString(6, comments);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Parcel");
    }
}