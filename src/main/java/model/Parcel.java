package model;

import java.math.BigDecimal;

public class Parcel {
    private int parcelId;
    private BigDecimal weight;
    private String description;
    private int orderId;
    private int invoice;
    private String comments;

    public Parcel() {
    }

    public Parcel(int parcelId, BigDecimal weight, String description, int orderId, int invoice, String comments) {
        this.parcelId = parcelId;
        this.weight = weight;
        this.description = description;
        this.orderId = orderId;
        this.invoice = invoice;
        this.comments = comments;
    }

    public int getParcelId() {
        return parcelId;
    }

    public void setParcelId(int parcelId) {
        this.parcelId = parcelId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "parcelId=" + parcelId +
                ", weight=" + weight +
                ", description='" + description + '\'' +
                ", orderId=" + orderId +
                ", invoice=" + invoice +
                ", comments='" + comments + '\'' +
                '}';
    }
}