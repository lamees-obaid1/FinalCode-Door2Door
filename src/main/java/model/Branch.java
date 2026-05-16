package model;

public class Branch {
    private int branchId;
    private String location;
    private String manager;
    private String status;

    public Branch() {
    }

    public Branch(int branchId, String location, String manager, String status) {
        this.branchId = branchId;
        this.location = location;
        this.manager = manager;
        this.status = status;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", location='" + location + '\'' +
                ", manager='" + manager + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}