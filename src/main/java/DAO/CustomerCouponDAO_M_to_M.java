package dao;

import java.sql.*;
import database.DataBaseConnection;

public class CustomerCouponDAO_M_to_M{ 

    public void insert(int customer_id, int coupon_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Customer_Coupon VALUES (?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, customer_id);
        ps.setInt(2, coupon_id);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Customer_Coupon");
    }

    public void delete(int customer_id, int coupon_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "DELETE FROM Customer_Coupon WHERE customer_id=? AND coupon_id=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, customer_id);
        ps.setInt(2, coupon_id);

        ps.executeUpdate();
    }
}