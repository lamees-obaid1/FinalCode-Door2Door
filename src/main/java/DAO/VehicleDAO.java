package dao;

import java.sql.*;
import database.DataBaseConnection;

public class VehicleDAO {

    public void insert(int vehicle_id, String type_of_car, String model, int year_of_issue,
                       String car_position, String driver_number, int driver_id) throws Exception {

        Connection con = DataBaseConnection.getConnection();

        String sql = "INSERT INTO Vehicle VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, vehicle_id);
        ps.setString(2, type_of_car);
        ps.setString(3, model);
        ps.setInt(4, year_of_issue);
        ps.setString(5, car_position);
        ps.setString(6, driver_number);
        ps.setInt(7, driver_id);

        ps.executeUpdate();
    }

    public ResultSet getAll() throws Exception {
        return DataBaseConnection.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Vehicle");
    }

    public void delete(int id) throws Exception {
        Connection con = DataBaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Vehicle WHERE vehicle_id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}