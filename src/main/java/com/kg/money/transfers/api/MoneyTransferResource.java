package com.kg.money.transfers.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.kg.money.transfers.storage.AccountStorage;

@Path("transfers")
public class MoneyTransferResource {

    private final AccountStorage accounts;

    public MoneyTransferResource(final AccountStorage accounts) {
        this.accounts = accounts;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "OK!";
    }
}
