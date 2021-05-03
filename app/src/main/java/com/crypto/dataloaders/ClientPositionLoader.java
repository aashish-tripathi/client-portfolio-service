package com.crypto.dataloaders;

import com.crypto.model.ClientPosition;
import com.crypto.service.ClientPortfolioManger;
import com.crypto.util.Constants;
import com.crypto.util.DataCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientPositionLoader {

    private static Logger logger = LogManager.getLogger(ClientPortfolioManger.class);
    private static DataCache dataCache = DataCache.getInstance();

    public static void loadInitialPositions(final String positionFile) {
        InputStream inputStream = ClientPositionLoader.class.getResourceAsStream(positionFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<ClientPosition> clientPositions = null;
        try {
            clientPositions  = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(clientPositions!=null){
            clientPositions.forEach( cp -> dataCache.addClientPosition(Constants.CLIENTID,cp));
            logger.info("Client Position has been loaded from file {}",positionFile);
        }

    }

    private static Function<String, ClientPosition> mapToItem = (line) -> {
        String[] p = line.split(",");
        ClientPosition position = new ClientPosition();
        position.setClientId(Constants.CLIENTID);
        position.setTicker(p[0]);
        position.setPositionSize(Long.valueOf(p[1]));
        return position;
    };

}
