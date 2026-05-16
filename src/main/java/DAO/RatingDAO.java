package dao;

import java.sql.*;
import database.DataBaseConnection;

public class RatingDAO {

    public void insert(int evaluation_id, int rate, String comments,
                       String date_of_evaluation, int customer_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Rating VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, evaluation_id);
        ps.setInt(2, rate);
        ps.setString(3, comments);
        ps.setString(4, date_of_evaluation);
        ps.setInt(5, customer_id);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Rating");
    }
}