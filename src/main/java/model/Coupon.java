package model;

import java.sql.Date;

public class Coupon {
    private int couponId;
    private String code;
    private Date expiryDate;

    public Coupon() {
    }

    public Coupon(int couponId, String code, Date expiryDate) {
        this.couponId = couponId;
        this.code = code;
        this.expiryDate = expiryDate;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponId=" + couponId +
                ", code='" + code + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}