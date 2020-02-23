package com.kg.money.transfers.api;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kg.money.transfers.config.ServiceConfiguration;
import com.kg.money.transfers.config.server.GrizzlyServer;
import com.kg.money.transfers.config.server.Server;
import com.kg.money.transfers.storage.FileAccountStorage;

class MoneyTransferResourceTest {

    private static final URI SERVER_URI = URI.create("http://localhost:8081/");
    private static Server SERVER;
    private WebTarget target;

    @BeforeAll
    static void setUp() throws Exception {
        ServiceConfiguration.configureLogging();
        SERVER = configureServer();
        SERVER.start();
    }

    private static Server configureServer () {
        final ResourceConfig resourceConfig = new ResourceConfig()
                .register(new MoneyTransferResource(new FileAccountStorage("test-accounts.json")));
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(SERVER_URI, resourceConfig);
        return new GrizzlyServer(server);
    }

    @BeforeEach
    public void set() {
        this.target = ClientBuilder.newClient().target(SERVER_URI);
    }

    @Test
    public void shouldGetBadRequestWhenInvalidJsonBodyInRequest() {
        final Response response = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{"));
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @AfterAll
    static void tearDown() {
        SERVER.stop();
    }
}