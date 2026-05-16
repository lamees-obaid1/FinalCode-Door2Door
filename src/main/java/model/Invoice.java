package model;

import java.math.BigDecimal;
import java.sql.Date;

public class Invoice {
    private int invoiceId;
    private Date date;
    private BigDecimal totalPrice;
    private int orderId;
    private BigDecimal discount;

    public Invoice() {
    }

    public Invoice(int invoiceId, Date date, BigDecimal totalPrice, int orderId, BigDecimal discount) {
        this.invoiceId = invoiceId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.discount = discount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", orderId=" + orderId +
                ", discount=" + discount +
                '}';
    }
}