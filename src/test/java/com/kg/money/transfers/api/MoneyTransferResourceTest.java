package com.kg.money.transfers.api;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
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

import com.kg.money.transfers.config.MoneyTransferServiceConfiguration;
import com.kg.money.transfers.config.server.GrizzlyServer;
import com.kg.money.transfers.config.server.Server;
import com.kg.money.transfers.storage.FileAccountStorage;

class MoneyTransferResourceTest {

    private static final URI SERVER_URI = URI.create("http://localhost:8081/");
    private static Server SERVER;
    private WebTarget target;

    @BeforeAll
    static void setUp() throws Exception {
        MoneyTransferServiceConfiguration.configureLogging();
        SERVER = configureServer();
        SERVER.start();
    }

    private static Server configureServer() {
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
    public void shouldGetBadRequestWhenMissingOrInvalidJsonBodyInRequest() {
        final Response invalidJsonResponse = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{"));
        final Response missingJsonResponse = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));
        assertThat(invalidJsonResponse.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
        assertThat(missingJsonResponse.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldGetBadRequestWhenMissingPropertyInRequest() {
        final Response response = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"from\": \"id-1\", \"to\": \"id-2\"}"));
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldGetBadRequestWhenInvalidTransferValue() {
        final Response response = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"from\": \"id-1\", \"to\": \"id-2\", \"amount\": -9}"));
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldGetBadRequestWhenSourceAndDestinationAccountIdsAreTheSame() {
        final Response response = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"from\": \"id-1\", \"to\": \"id-1\", \"amount\": 10}"));
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldGetBadRequestWhenSourceOrDestinationAccountIdsDoNotExist() {
        final Response nonExistingSourceAccountIdResponse = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"from\": \"id-10\", \"to\": \"id-1\", \"amount\": 10}"));
        final Response nonExistingDestinationAccountIdResponse = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"from\": \"id-1\", \"to\": \"id-10\", \"amount\": 10}"));
        assertThat(nonExistingSourceAccountIdResponse.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
        assertThat(nonExistingDestinationAccountIdResponse.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldTransferMoney() {
        final Response response = this.target.path("transfers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json("{\"from\": \"id-1\", \"to\": \"id-2\", \"amount\": 10}"));
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    }

    @AfterAll
    static void tearDown() {
        SERVER.stop();
    }
}