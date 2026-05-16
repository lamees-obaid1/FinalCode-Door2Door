package model;

import java.math.BigDecimal;
import java.sql.Date;

public class Order {
    private int orderId;
    private Date dateOfOrder;
    private String deliveryAddress;
    private BigDecimal totalPrice;
    private String orderStatus;
    private int customerId;
    private int driverId;
    private int branchId;

    public Order() {
    }

    public Order(int orderId, Date dateOfOrder, String deliveryAddress, BigDecimal totalPrice,
                 String orderStatus, int customerId, int driverId, int branchId) {
        this.orderId = orderId;
        this.dateOfOrder = dateOfOrder;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.driverId = driverId;
        this.branchId = branchId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", dateOfOrder=" + dateOfOrder +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderStatus='" + orderStatus + '\'' +
                ", customerId=" + customerId +
                ", driverId=" + driverId +
                ", branchId=" + branchId +
                '}';
    }
}