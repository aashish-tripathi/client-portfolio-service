package com.crypto.model;

public class MarketPrice {

    private double price;
    private boolean refreshed;

    public MarketPrice() {
    }

    public MarketPrice(double price, boolean refreshed) {
        this.price = price;
        this.refreshed = refreshed;
    }

    public double getPrice() {
        return price;
    }

    public synchronized void setPrice(double price) {
        this.price = price;
    }

    public boolean isRefreshed() {
        return refreshed;
    }

    public synchronized void setRefreshed(boolean refreshed) {
        this.refreshed = refreshed;
    }

    @Override
    public String toString() {
        return "MarketPrice{" +
                "price=" + price +
                ", refreshed=" + refreshed +
                '}';
    }
}
