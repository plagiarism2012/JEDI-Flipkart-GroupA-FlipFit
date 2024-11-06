package com.flipkart.bean;

public class FlipFitPayment {
    private int CardId;
    private String userId;
    private String cardNumber;
    private int cvv;
    private String exp;

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCardId() {
        return CardId;
    }

    public void setId(int id) {
        this.CardId = id;
    }

}
