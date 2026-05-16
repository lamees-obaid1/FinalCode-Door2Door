package dao;

import java.sql.*;
import database.DataBaseConnection;

public class BranchDAO {

    public void insert(int branch_id, String location, String manager, String status) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Branch VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, branch_id);
        ps.setString(2, location);
        ps.setString(3, manager);
        ps.setString(4, status);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Branch");
    }

    public void delete(int id) throws Exception {
        PreparedStatement ps = DataBaseConnection.getConnection()
                .prepareStatement("DELETE FROM Branch WHERE branch_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}