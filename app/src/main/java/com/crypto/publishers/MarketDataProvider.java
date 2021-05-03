package com.crypto.publishers;

import com.crypto.model.ClientPosition;
import com.crypto.model.MarketPrice;
import com.crypto.model.SecurityType;
import com.crypto.util.DataCache;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarketDataProvider implements Runnable {

    public static final int LARGE_CAP = 400;
    private AtomicBoolean running = new AtomicBoolean(true);
    private String[] tickers;

    private DataCache dataCache = DataCache.getInstance();

    public MarketDataProvider() {
        this.tickers = new String[dataCache.getTickers().size()];
        this.subscribe(dataCache.getTickers());
    }

    private void subscribe(List<String> tickerList) {
        tickers =   tickerList.toArray(new String[0]);
    }

    @Override
    public void run() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        String ticker = tickers[localRandom.nextInt(tickers.length)];
        generateNewPriceFor(ticker);
    }
    private void generateNewPriceFor(String ticker) {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        double deltaChange = localRandom.nextLong(10);
        double lastPrice = dataCache.getMarketPrice(ticker).getPrice();

        deltaChange = localRandom.nextInt(2) == 0 ? -(deltaChange) : deltaChange;
        double newPrice = lastPrice + deltaChange;

        List<ClientPosition> clientPositions = dataCache.getClientPositionsFor(ticker);
        double finalDeltaChange = deltaChange;
        clientPositions.forEach(cp ->{
            if(cp.getType()!= SecurityType.stock){
                if(lastPrice < 200 ) {
                    refreshOptionPricing(finalDeltaChange, cp, 0.01);
                }else{
                    refreshOptionPricing(finalDeltaChange, cp, 0.05);
                }

            }else{
                cp.setMarketPrice(new MarketPrice((newPrice),true));
            }
        });
    }

    private void refreshOptionPricing(double finalDeltaChange, ClientPosition cp, double v) {
        if (cp.getType() == SecurityType.call) {
            double callPrice = (0.05 * finalDeltaChange);
            cp.setMarketPrice(new MarketPrice((callPrice + cp.getMarketPrice().getPrice()), false));
        } else {
            double putPrice = (-v) * finalDeltaChange;
            cp.setMarketPrice(new MarketPrice((putPrice + cp.getMarketPrice().getPrice()), false));
        }
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public void setRunning(AtomicBoolean running) {
        this.running = running;
    }
}
