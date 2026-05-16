package model;

import java.math.BigDecimal;

public class Driver {
    private int driverId;
    private String name;
    private BigDecimal salary;
    private String location;
    private String phone;

    public Driver() {
    }

    public Driver(int driverId, String name, BigDecimal salary, String location, String phone) {
        this.driverId = driverId;
        this.name = name;
        this.salary = salary;
        this.location = location;
        this.phone = phone;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}