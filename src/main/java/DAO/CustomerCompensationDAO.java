package dao;

import java.sql.*;
import database.DataBaseConnection;

public class CustomerCompensationDAO {

    public void insert(int compensation_id, int customer_id, int order_id,
                       String type_of_Compensation, double Compensation_value,
                       String reason_of_Compensation, String approval_status,
                       int employee_who_agreed, String order_status,
                       String date_of_approval, String comments) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO `Customer Compensation` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, compensation_id);
        ps.setInt(2, customer_id);
        ps.setInt(3, order_id);
        ps.setString(4, type_of_Compensation);
        ps.setDouble(5, Compensation_value);
        ps.setString(6, reason_of_Compensation);
        ps.setString(7, approval_status);
        ps.setInt(8, employee_who_agreed);
        ps.setString(9, order_status);
        ps.setString(10, date_of_approval);
        ps.setString(11, comments);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM `Customer Compensation`");
    }
}