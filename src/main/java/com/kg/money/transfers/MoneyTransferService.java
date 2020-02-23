package com.kg.money.transfers;

import com.kg.money.transfers.config.MoneyTransferServiceConfiguration;
import com.kg.money.transfers.config.server.Server;


public class MoneyTransferService {

    private final Server server;

    public MoneyTransferService(final Server server) {
        this.server = server;
    }

    public void run() throws Exception {
        this.server.start();
    }

    public static void main(String[] args) throws Exception {
       MoneyTransferServiceConfiguration.configureLogging();
       new MoneyTransferService(MoneyTransferServiceConfiguration.configureServer()).run();
    }
}
