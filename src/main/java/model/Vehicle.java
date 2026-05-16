package model;

public class Vehicle {
    private int vehicleId;
    private String typeOfCar;
    private String model;
    private int yearOfIssue;
    private String carPosition;
    private String driverNumber;
    private int driverId;

    public Vehicle() {
    }

    public Vehicle(int vehicleId, String typeOfCar, String model, int yearOfIssue,
                   String carPosition, String driverNumber, int driverId) {
        this.vehicleId = vehicleId;
        this.typeOfCar = typeOfCar;
        this.model = model;
        this.yearOfIssue = yearOfIssue;
        this.carPosition = carPosition;
        this.driverNumber = driverNumber;
        this.driverId = driverId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getTypeOfCar() {
        return typeOfCar;
    }

    public void setTypeOfCar(String typeOfCar) {
        this.typeOfCar = typeOfCar;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(int yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public String getCarPosition() {
        return carPosition;
    }

    public void setCarPosition(String carPosition) {
        this.carPosition = carPosition;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", typeOfCar='" + typeOfCar + '\'' +
                ", model='" + model + '\'' +
                ", yearOfIssue=" + yearOfIssue +
                ", carPosition='" + carPosition + '\'' +
                ", driverNumber='" + driverNumber + '\'' +
                ", driverId=" + driverId +
                '}';
    }
}