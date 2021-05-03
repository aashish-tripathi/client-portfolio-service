package com.crypto.model;

import java.math.BigDecimal;

public class ClientPosition {

    private String clientId;
    private String ticker;
    private long positionSize;
    private SecurityType type;
    private MarketPrice marketPrice;
    private BigDecimal positionValue;

    public ClientPosition() {
    }

    public ClientPosition(String ticker, long positionSize) {
        this.ticker = ticker;
        this.positionSize = positionSize;
    }

    public MarketPrice getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(MarketPrice marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(BigDecimal positionValue) {
        this.positionValue = positionValue;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public long getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(long positionSize) {
        this.positionSize = positionSize;
    }

    public SecurityType getType() {
        return type;
    }

    public void setType(SecurityType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClientPosition{" +
                "clientId='" + clientId + '\'' +
                ", ticker='" + ticker + '\'' +
                ", positionSize=" + positionSize +
                ", type=" + type +
                ", marketPrice=" + marketPrice +
                ", positionValue=" + positionValue +
                '}';
    }
}
