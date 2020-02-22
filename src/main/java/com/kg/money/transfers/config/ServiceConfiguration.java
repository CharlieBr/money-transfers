package com.kg.money.transfers.config;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.kg.money.transfers.api.MoneyTransferResource;
import com.kg.money.transfers.config.server.GrizzlyServer;
import com.kg.money.transfers.config.server.Server;

public class ServiceConfiguration {
    public static final URI APP_BASE_URI = URI.create("http://localhost:8080/");

    public Server configureServer() {
        final ResourceConfig resourceConfig = new ResourceConfig(MoneyTransferResource.class);
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(APP_BASE_URI, resourceConfig);
        return new GrizzlyServer(server);
    }
}
