package dao;

import java.sql.*;
import database.DataBaseConnection;

public class TrackingDAO {

    public void insert(int tracking_id, int order_id, String order_status,
                       String order_location, String update_time,
                       String driver, String comments) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Tracking VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, tracking_id);
        ps.setInt(2, order_id);
        ps.setString(3, order_status);
        ps.setString(4, order_location);
        ps.setString(5, update_time);
        ps.setString(6, driver);
        ps.setString(7, comments);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Tracking");
    }
}