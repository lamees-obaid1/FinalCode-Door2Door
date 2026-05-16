package model;

import java.math.BigDecimal;

public class Complaint {
    private int complaintId;
    private BigDecimal requiredPrice;
    private BigDecimal pricePaid;
    private BigDecimal restOfMount;
    private int invoiceId;

    public Complaint() {
    }

    public Complaint(int complaintId, BigDecimal requiredPrice, BigDecimal pricePaid,
                     BigDecimal restOfMount, int invoiceId) {
        this.complaintId = complaintId;
        this.requiredPrice = requiredPrice;
        this.pricePaid = pricePaid;
        this.restOfMount = restOfMount;
        this.invoiceId = invoiceId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
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
        return "Complaint{" +
                "complaintId=" + complaintId +
                ", requiredPrice=" + requiredPrice +
                ", pricePaid=" + pricePaid +
                ", restOfMount=" + restOfMount +
                ", invoiceId=" + invoiceId +
                '}';
    }
}