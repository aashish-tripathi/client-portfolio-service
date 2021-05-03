package com.crypto.util;

import com.crypto.model.ClientPosition;
import com.crypto.model.MarketPrice;
import com.crypto.model.SecurityDefinition;
import com.crypto.model.SecurityType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataCache {

    private ConcurrentMap<String, List<ClientPosition>> clientPositionsMap = new ConcurrentHashMap<>();
    private Map<String, SecurityDefinition> securityDataMap = new HashMap<>();
    private static DataCache instance = new DataCache();

    private DataCache() {

    }

    public static DataCache getInstance() {
        return instance;
    }

    public List<ClientPosition> getAllClientPositions() {
        return clientPositionsMap.get(Constants.CLIENTID);
    }

    public void addClientPosition(final String clientId, final ClientPosition position ){
        clientPositionsMap.computeIfAbsent(clientId, k-> new ArrayList<ClientPosition>()).add(position);
    }

    public List<ClientPosition> getClientPositionsFor(final String ticker) {
        return clientPositionsMap.get(Constants.CLIENTID).stream().filter(p-> p.getTicker().contains(ticker)).collect(Collectors.toList());
    }

    public List<String> getTickers() {
        List<String> tickers = new ArrayList<>();
        securityDataMap.values().stream().filter(s -> s.getType()== SecurityType.stock).forEach(x -> tickers.add(x.getTicker()));;
        return  tickers;
    }

    public void refreshClientPosition(SecurityDefinition definition) {
        clientPositionsMap.get(Constants.CLIENTID).stream().filter(x-> x.getTicker().equals(definition.getTicker())).forEach(it -> it.setType(definition.getType()));
        clientPositionsMap.get(Constants.CLIENTID).stream().filter(x-> (x.getTicker().equals(definition.getTicker()))).forEach(it -> it.setMarketPrice(new MarketPrice(definition.getStrikePrice(),true)));
    }

    public MarketPrice getMarketPrice(final String ticker) {
        return clientPositionsMap.get(Constants.CLIENTID).stream().filter(x-> x.getTicker().equals(ticker) && x.getType()==SecurityType.stock).findFirst().get().getMarketPrice();
    }

    public void addSecurity(final String ticker, final SecurityDefinition securityDefinition){
        securityDataMap.put(ticker, securityDefinition);

    }

    public boolean isMarketPriceUpdated(){
        return  clientPositionsMap.get(Constants.CLIENTID).stream().anyMatch(x-> x.getMarketPrice().isRefreshed()==true);
    }


    public SecurityDefinition getSecurityData(final String ticker){
        return securityDataMap.get(ticker);
    }
}
