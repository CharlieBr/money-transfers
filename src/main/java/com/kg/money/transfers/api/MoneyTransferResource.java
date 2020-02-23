package com.kg.money.transfers.api;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kg.money.transfers.model.MoneyTransferRequest;
import com.kg.money.transfers.storage.AccountStorage;

@Path("transfers")
public class MoneyTransferResource {

    private final AccountStorage accounts;

    public MoneyTransferResource(final AccountStorage accounts) {
        this.accounts = accounts;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferMoney(final String body) {
        try {
            final MoneyTransferRequest moneyTransferRequest = MoneyTransferRequest.fromJson(body);
        }
        catch(final JsonProcessingException e) {
            return Response.status(BAD_REQUEST).entity("Invalid JSON in request body").build();
        }
        return Response.ok().build();
    }
}
