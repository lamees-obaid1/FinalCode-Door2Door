package model;

import java.sql.Timestamp;

public class Tracking {
    private int trackingId;
    private int orderId;
    private String orderStatus;
    private String orderLocation;
    private Timestamp updateTime;
    private String driver;
    private String comments;

    public Tracking() {
    }

    public Tracking(int trackingId, int orderId, String orderStatus, String orderLocation,
                    Timestamp updateTime, String driver, String comments) {
        this.trackingId = trackingId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderLocation = orderLocation;
        this.updateTime = updateTime;
        this.driver = driver;
        this.comments = comments;
    }

    public int getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(int trackingId) {
        this.trackingId = trackingId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderLocation() {
        return orderLocation;
    }

    public void setOrderLocation(String orderLocation) {
        this.orderLocation = orderLocation;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Tracking{" +
                "trackingId=" + trackingId +
                ", orderId=" + orderId +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderLocation='" + orderLocation + '\'' +
                ", updateTime=" + updateTime +
                ", driver='" + driver + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}