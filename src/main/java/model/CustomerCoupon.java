package model;

public class CustomerCoupon {
    private int customerId;
    private int couponId;

    public CustomerCoupon() {
    }

    public CustomerCoupon(int customerId, int couponId) {
        this.customerId = customerId;
        this.couponId = couponId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "CustomerCoupon{" +
                "customerId=" + customerId +
                ", couponId=" + couponId +
                '}';
    }
}