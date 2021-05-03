package com.crypto.dataloaders;

import com.crypto.model.MarketPrice;
import com.crypto.model.SecurityDefinition;
import com.crypto.model.SecurityType;
import com.crypto.subscribers.PortfolioResultSubscriber;
import com.crypto.util.DataCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;

public class SecurityLoader {

    private static Logger logger = LogManager.getLogger(PortfolioResultSubscriber.class);
    private static DataCache dataCache = DataCache.getInstance();

    public static void loadSecurityData(final Properties properties) {
        final String jdbcUrl = properties.getProperty("crypto.datasource.url");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            loadSecurities(connection, properties);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    private static void loadSecurities(final Connection connection, final Properties properties) throws SQLException {
        final String dropStatement = properties.getProperty("crypto.datasource.delete");
        final String createTable = properties.getProperty("crypto.datasource.create");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        statement.executeUpdate(dropStatement);
        statement.executeUpdate(createTable);

        final String []insertQueries = properties.getProperty("crypto.datasource.inserts").split(":");
        for (int i = 0; i < insertQueries.length; i++) {
            String insertQuery =  insertQueries[i];
            statement.executeUpdate(insertQuery);
        }

        final String selectQuery = properties.getProperty("crypto.datasource.select");
        ResultSet rs = statement.executeQuery(selectQuery);

        while (rs.next()) {
            // read the result set and make in available in cache
            SecurityDefinition definition = new SecurityDefinition();
            long Id = rs.getInt("id");
            String ticker = rs.getString("ticker");
            definition.setId(Id);
            definition.setTicker(ticker);
            definition.setType(SecurityType.valueOf(rs.getString("type")));
            double price = rs.getFloat("strikePrice");
            definition.setStrikePrice(price);
            definition.setMaturityDate(rs.getString("maturityDate"));
            dataCache.addSecurity(ticker, definition);
            // sync with price and other static info
            dataCache.refreshClientPosition(definition);
        }
        logger.info("Security has been loaded from SQLLite DB {}");
    }
}
