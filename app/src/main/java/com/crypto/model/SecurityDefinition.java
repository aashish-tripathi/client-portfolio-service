package com.crypto.model;


public class SecurityDefinition {

    private long Id;
    private String ticker;
    private SecurityType type;
    private double strikePrice;
    private String maturityDate;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public SecurityType getType() {
        return type;
    }

    public void setType(SecurityType type) {
        this.type = type;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    @Override
    public String toString() {
        return "SecurityDefinition{" +
                "Id=" + Id +
                ", ticker='" + ticker + '\'' +
                ", type=" + type +
                ", strikePrice=" + strikePrice +
                ", maturityDate='" + maturityDate + '\'' +
                '}';
    }
}
