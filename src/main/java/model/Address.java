package model;

public class Address {
    private int addressId;
    private int customerNumber;
    private String city;
    private String street;
    private String building;
    private String apartmentNumber;

    public Address() {
    }

    public Address(int addressId, int customerNumber, String city, String street,
                   String building, String apartmentNumber) {
        this.addressId = addressId;
        this.customerNumber = customerNumber;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartmentNumber = apartmentNumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", customerNumber=" + customerNumber +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", building='" + building + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                '}';
    }
}