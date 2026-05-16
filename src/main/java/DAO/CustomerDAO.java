package dao;

import java.sql.*;
import database.DataBaseConnection;
import model.Customer;

public class CustomerDAO {

    public void insert(Customer c) throws Exception {
        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Customer(customer_id, name, address, email) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, c.getCustomerId());
        ps.setString(2, c.getName());
        ps.setString(3, c.getAddress());
        ps.setString(4, c.getEmail());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public ResultSet getAll() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        return con.createStatement().executeQuery("SELECT * FROM Customer");
    }

    public void update(Customer c) throws Exception {
        Connection con = DataBaseConnection.getConnection();

        String sql = "UPDATE Customer SET name=?, address=?, email=? WHERE customer_id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, c.getName());
        ps.setString(2, c.getAddress());
        ps.setString(3, c.getEmail());
        ps.setInt(4, c.getCustomerId());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws Exception {
        Connection con = DataBaseConnection.getConnection();

        try {
            con.setAutoCommit(false);

            PreparedStatement psAddress =
                    con.prepareStatement("DELETE FROM address WHERE customer_number=?");
            psAddress.setInt(1, id);
            psAddress.executeUpdate();

            PreparedStatement psCustomer =
                    con.prepareStatement("DELETE FROM Customer WHERE customer_id=?");
            psCustomer.setInt(1, id);
            psCustomer.executeUpdate();

            con.commit();

            psAddress.close();
            psCustomer.close();

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }
}