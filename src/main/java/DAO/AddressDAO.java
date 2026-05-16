package dao;

import java.sql.*;
import database.DataBaseConnection;

public class AddressDAO {

    public void insert(int address_id, int customer_number, String city,
                       String street, String building, String apartment_number) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Address VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, address_id);
        ps.setInt(2, customer_number);
        ps.setString(3, city);
        ps.setString(4, street);
        ps.setString(5, building);
        ps.setString(6, apartment_number);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Address");
    }

    public void delete(int id) throws Exception {
        PreparedStatement ps = DataBaseConnection.getConnection()
                .prepareStatement("DELETE FROM Address WHERE address_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}