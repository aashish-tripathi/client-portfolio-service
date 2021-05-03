# client-portfolio-service

This application has been distributed in following package

1.  dataloaders - Use to loaded security data and existing client position
2.  model- Model/Objet being used across application
3.  publisher - market data provider
4.  service - Driver class
5.  subscriber - Result to be printed on console
6.  util - Internal cache for some state management(security, client position).
7.  resource - clientposition csv , SQLLite db configuration & log4j config

Dependency- only log4j and sql lite jdbc

Embedded Database- crypto_db.db
Table              Security

Build- In gradle

How to run-

start App.java to kick start process and result will be printed out in console (pretty print)

Note: I have also attached screenshots of the results from this application output.






