package model;

import java.math.BigDecimal;

public class Payment {
    private int paymentId;
    private String paymentWay;
    private BigDecimal requiredPrice;
    private BigDecimal pricePaid;
    private BigDecimal restOfMount;
    private int invoiceId;

    public Payment() {
    }

    public Payment(int paymentId, String paymentWay, BigDecimal requiredPrice,
                   BigDecimal pricePaid, BigDecimal restOfMount, int invoiceId) {
        this.paymentId = paymentId;
        this.paymentWay = paymentWay;
        this.requiredPrice = requiredPrice;
        this.pricePaid = pricePaid;
        this.restOfMount = restOfMount;
        this.invoiceId = invoiceId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public BigDecimal getRequiredPrice() {
        return requiredPrice;
    }

    public void setRequiredPrice(BigDecimal requiredPrice) {
        this.requiredPrice = requiredPrice;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
    }

    public BigDecimal getRestOfMount() {
        return restOfMount;
    }

    public void setRestOfMount(BigDecimal restOfMount) {
        this.restOfMount = restOfMount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentWay='" + paymentWay + '\'' +
                ", requiredPrice=" + requiredPrice +
                ", pricePaid=" + pricePaid +
                ", restOfMount=" + restOfMount +
                ", invoiceId=" + invoiceId +
                '}';
    }
}