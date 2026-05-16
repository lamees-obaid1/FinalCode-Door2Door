package dao;

import java.sql.*;
import database.DataBaseConnection;

public class PaymentMethodDAO {

    public void insert(int method_id, boolean cash,
                       boolean credit_card, boolean wallet) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO `Payment Method` VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, method_id);
        ps.setBoolean(2, cash);
        ps.setBoolean(3, credit_card);
        ps.setBoolean(4, wallet);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM `Payment Method`");
    }
}