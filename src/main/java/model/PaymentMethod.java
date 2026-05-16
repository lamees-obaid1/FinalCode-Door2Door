package model;

public class PaymentMethod {
    private int methodId;
    private boolean cash;
    private boolean creditCard;
    private boolean wallet;

    public PaymentMethod() {
    }

    public PaymentMethod(int methodId, boolean cash, boolean creditCard, boolean wallet) {
        this.methodId = methodId;
        this.cash = cash;
        this.creditCard = creditCard;
        this.wallet = wallet;
    }

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public boolean isWallet() {
        return wallet;
    }

    public void setWallet(boolean wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "methodId=" + methodId +
                ", cash=" + cash +
                ", creditCard=" + creditCard +
                ", wallet=" + wallet +
                '}';
    }
}