package test;

import model.Customer;
import dao.CustomerDAO;

public class TestCustomer {

    public static void main(String[] args) {
        try {
            Customer c = new Customer(6, "shahed", "Nablus", "sh@gmail.com");

            new CustomerDAO().insert(c);

            System.out.println("Inserted Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}