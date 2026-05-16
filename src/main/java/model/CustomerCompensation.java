package model;

import java.math.BigDecimal;
import java.sql.Date;

public class CustomerCompensation {
    private int compensationId;
    private int customerId;
    private int orderId;
    private String typeOfCompensation;
    private BigDecimal compensationValue;
    private String reasonOfCompensation;
    private String approvalStatus;
    private int employeeWhoAgreed;
    private String orderStatus;
    private Date dateOfApproval;
    private String comments;

    public CustomerCompensation() {
    }

    public CustomerCompensation(int compensationId, int customerId, int orderId,
                                String typeOfCompensation, BigDecimal compensationValue,
                                String reasonOfCompensation, String approvalStatus,
                                int employeeWhoAgreed, String orderStatus,
                                Date dateOfApproval, String comments) {
        this.compensationId = compensationId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.typeOfCompensation = typeOfCompensation;
        this.compensationValue = compensationValue;
        this.reasonOfCompensation = reasonOfCompensation;
        this.approvalStatus = approvalStatus;
        this.employeeWhoAgreed = employeeWhoAgreed;
        this.orderStatus = orderStatus;
        this.dateOfApproval = dateOfApproval;
        this.comments = comments;
    }

    public int getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(int compensationId) {
        this.compensationId = compensationId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTypeOfCompensation() {
        return typeOfCompensation;
    }

    public void setTypeOfCompensation(String typeOfCompensation) {
        this.typeOfCompensation = typeOfCompensation;
    }

    public BigDecimal getCompensationValue() {
        return compensationValue;
    }

    public void setCompensationValue(BigDecimal compensationValue) {
        this.compensationValue = compensationValue;
    }

    public String getReasonOfCompensation() {
        return reasonOfCompensation;
    }

    public void setReasonOfCompensation(String reasonOfCompensation) {
        this.reasonOfCompensation = reasonOfCompensation;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public int getEmployeeWhoAgreed() {
        return employeeWhoAgreed;
    }

    public void setEmployeeWhoAgreed(int employeeWhoAgreed) {
        this.employeeWhoAgreed = employeeWhoAgreed;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDateOfApproval() {
        return dateOfApproval;
    }

    public void setDateOfApproval(Date dateOfApproval) {
        this.dateOfApproval = dateOfApproval;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "CustomerCompensation{" +
                "compensationId=" + compensationId +
                ", customerId=" + customerId +
                ", orderId=" + orderId +
                ", typeOfCompensation='" + typeOfCompensation + '\'' +
                ", compensationValue=" + compensationValue +
                ", reasonOfCompensation='" + reasonOfCompensation + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", employeeWhoAgreed=" + employeeWhoAgreed +
                ", orderStatus='" + orderStatus + '\'' +
                ", dateOfApproval=" + dateOfApproval +
                ", comments='" + comments + '\'' +
                '}';
    }
}