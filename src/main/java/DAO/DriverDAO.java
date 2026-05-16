package dao;

import java.sql.*;
import database.DataBaseConnection;

public class DriverDAO {

    public void insert(int driver_id, String name, double salary, String location, String phone) throws Exception {
        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Driver(driver_id, name, salary, location, phone) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, driver_id);
        ps.setString(2, name);
        ps.setDouble(3, salary);
        ps.setString(4, location);
        ps.setString(5, phone);

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public ResultSet getAll() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        return con.createStatement().executeQuery("SELECT * FROM Driver");
    }

    public void update(int driver_id, String name, double salary, String location, String phone) throws Exception {
        Connection con = DataBaseConnection.getConnection();

        String sql = "UPDATE Driver SET name=?, salary=?, location=?, phone=? WHERE driver_id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setDouble(2, salary);
        ps.setString(3, location);
        ps.setString(4, phone);
        ps.setInt(5, driver_id);

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws Exception {
        Connection con = DataBaseConnection.getConnection();

        PreparedStatement ps = con.prepareStatement("DELETE FROM Driver WHERE driver_id=?");
        ps.setInt(1, id);

        ps.executeUpdate();

        ps.close();
        con.close();
    }
}