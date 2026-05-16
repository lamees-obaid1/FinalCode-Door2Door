package test;

import java.sql.Connection;
import database.DataBaseConnection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection con = DataBaseConnection.getConnection();

            if (con != null) {
                System.out.println("Connection SUCCESS");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}