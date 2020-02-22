package com.kg.money.transfers.config.server;

import org.glassfish.grizzly.http.server.HttpServer;

public class GrizzlyServer implements Server {

    private final HttpServer server;

    public GrizzlyServer(final HttpServer server) {
        this.server = server;
    }

    public void start() throws Exception {
        this.server.start();
    }
}
