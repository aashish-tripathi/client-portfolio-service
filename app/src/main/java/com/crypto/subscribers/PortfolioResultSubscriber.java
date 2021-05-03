package com.crypto.subscribers;

import com.crypto.model.ClientPosition;
import com.crypto.model.SecurityType;
import com.crypto.util.DataCache;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PortfolioResultSubscriber implements Runnable {

    private AtomicBoolean running = new AtomicBoolean(true);
    private DataCache dataCache = DataCache.getInstance();

    @Override
    public void run() {
        if (dataCache.isMarketPriceUpdated()) {
            List<ClientPosition> positionList = dataCache.getAllClientPositions();
            positionList.forEach(position -> {
                if (position.getMarketPrice().isRefreshed() && position.getType() == SecurityType.stock) {
                    System.out.format("%-12s%2s%12f\n", position.getTicker(), "changed to", position.getMarketPrice().getPrice());
                }
                position.getMarketPrice().setRefreshed(false);
            });
            System.out.format("\n");
            System.out.format("%-34s\n", "## Portfolio");
            System.out.format("%-34s%16s%16s%24s\n", "Symbol", "Price", "Qty", "Value");
            AtomicReference<Double> totalPortfolioValue = new AtomicReference<>(0.0);
            positionList.forEach(position -> {
                System.out.format("%-32s%20f%16d%24f\n", position.getTicker(), position.getMarketPrice().getPrice(), position.getPositionSize(), position.getPositionValue().floatValue());
                totalPortfolioValue.updateAndGet(v -> new Double((double) (v + position.getPositionValue().floatValue())));
            });
            System.out.format("\n");
            System.out.format("%-34s%24f\n", "# Total Portfolio", totalPortfolioValue.get());
            System.out.format("\n");
        }
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public void setRunning(AtomicBoolean running) {
        this.running = running;
    }

}
