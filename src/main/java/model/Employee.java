package model;

public class Employee {
    private int employeeId;
    private String name;
    private String email;
    private int branch;

    public Employee() {
    }

    public Employee(int employeeId, String name, String email, int branch) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.branch = branch;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", branch=" + branch +
                '}';
    }
}