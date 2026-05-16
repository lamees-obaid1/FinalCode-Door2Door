import database.DataBaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DataBaseConnection.getConnection();

            if (conn != null) {
                System.out.println("Connected to database successfully!");
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}