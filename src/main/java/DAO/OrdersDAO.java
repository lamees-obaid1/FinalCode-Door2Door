package dao;

import java.sql.*;
import database.DataBaseConnection;

public class OrdersDAO {

    public void insert(int order_id, String date_of_order, String delivery_address,
                       double total_price, String order_status,
                       int customer_id, int driver_id, int branch_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, order_id);
        ps.setString(2, date_of_order);
        ps.setString(3, delivery_address);
        ps.setDouble(4, total_price);
        ps.setString(5, order_status);
        ps.setInt(6, customer_id);
        ps.setInt(7, driver_id);
        ps.setInt(8, branch_id);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Orders");
    }

    public void delete(int id) throws Exception {
        PreparedStatement ps = DataBaseConnection.getConnection()
                .prepareStatement("DELETE FROM Orders WHERE order_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}