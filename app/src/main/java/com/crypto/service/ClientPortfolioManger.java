package com.crypto.service;

import com.crypto.dataloaders.ClientPositionLoader;
import com.crypto.dataloaders.SecurityLoader;
import com.crypto.model.ClientPosition;
import com.crypto.publishers.MarketDataProvider;
import com.crypto.subscribers.PortfolioResultSubscriber;
import com.crypto.util.DataCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientPortfolioManger {

    public static final int DELAY = 10000;
    private AtomicBoolean runningStatus = new AtomicBoolean(true);
    private DataCache dataCache= DataCache.getInstance();
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    private static Logger logger = LogManager.getLogger(ClientPortfolioManger.class);

    public ClientPortfolioManger(final String initialPositionFile, final Properties properties) {
        ClientPositionLoader.loadInitialPositions(initialPositionFile);
        SecurityLoader.loadSecurityData(properties);
        service.scheduleWithFixedDelay(new PortfolioResultSubscriber(),5, 10, TimeUnit.SECONDS);
        service.scheduleWithFixedDelay(new MarketDataProvider(),10, 10, TimeUnit.SECONDS);
    }

    public void start() {
        while (getRunningStatus().get()) {
            //  On market price update, recalculate client position
            if (dataCache.isMarketPriceUpdated()) {
                List<ClientPosition> clientPositions = dataCache.getAllClientPositions();
                clientPositions.forEach(cp -> {
                    cp.setPositionValue(new BigDecimal(cp.getPositionSize() * cp.getMarketPrice().getPrice()));
                });
                logger.info("Position has been updated {}",clientPositions);
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public AtomicBoolean getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(AtomicBoolean runningStatus) {
        this.runningStatus = runningStatus;
    }
}
