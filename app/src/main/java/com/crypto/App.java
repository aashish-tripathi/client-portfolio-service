package com.crypto;

import com.crypto.service.ClientPortfolioManger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            InputStream inputStream = App.class.getResourceAsStream("/db.properties");
            final Properties properties = new Properties();
            properties.load(inputStream);
            final String positionFile = "/clientposition.csv";
            new ClientPortfolioManger(positionFile, properties).start();
        }
    }
}
