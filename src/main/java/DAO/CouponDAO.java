package dao;

import java.sql.*;
import database.DataBaseConnection;

public class CouponDAO {

    public void insert(int coupon_id, String code, String expiry_date) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Coupon VALUES (?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, coupon_id);
        ps.setString(2, code);
        ps.setString(3, expiry_date);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Coupon");
    }

    public void delete(int id) throws Exception {
        PreparedStatement ps = DataBaseConnection.getConnection()
                .prepareStatement("DELETE FROM Coupon WHERE coupon_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}