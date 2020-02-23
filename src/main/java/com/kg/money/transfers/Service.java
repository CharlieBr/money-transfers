package com.kg.money.transfers;

import com.kg.money.transfers.config.ServiceConfiguration;
import com.kg.money.transfers.config.server.Server;


public class Service {

    private final Server server;

    public Service(final Server server) {
        this.server = server;
    }

    public void run() throws Exception {
        this.server.start();
    }

    public static void main(String[] args) throws Exception {
       final ServiceConfiguration config = new ServiceConfiguration();
       config.configureLogging();
       new Service(config.configureServer()).run();
    }
}
