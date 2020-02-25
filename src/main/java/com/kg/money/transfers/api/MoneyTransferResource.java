package com.kg.money.transfers.api;

import static com.kg.money.transfers.validators.MoneyTransferRequestValidator.checkIfDifferentAccounts;
import static com.kg.money.transfers.validators.MoneyTransferRequestValidator.validateExistingAccount;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kg.money.transfers.model.Account;
import com.kg.money.transfers.model.MoneyTransferRequest;
import com.kg.money.transfers.accounts.Accounts;
import com.kg.money.transfers.model.UpdatedAccounts;

@Path("transfers")
public class MoneyTransferResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyTransferResource.class);
    private final Accounts accounts;

    public MoneyTransferResource(final Accounts accounts) {
        this.accounts = accounts;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferMoney(final String body) {
        try {
            final MoneyTransferRequest request = MoneyTransferRequest.fromJson(body);
            final UpdatedAccounts updatedAccounts = doTransfer(request);
            return createOkResponse(updatedAccounts);
        }
        catch(final IllegalArgumentException e) {
            return createBadRequestResponse(e.getMessage());
        }
    }

    private UpdatedAccounts doTransfer(final MoneyTransferRequest request) {
        final String sourceAccountId = validateExistingAccount(request.getSourceAccountId(), this.accounts);
        final String destinationAccountId = validateExistingAccount(request.getDestinationAccountId(), this.accounts);
        checkIfDifferentAccounts(sourceAccountId, destinationAccountId);
        return this.accounts.transfer(sourceAccountId, destinationAccountId, request.getAmount());
    }

    private static Response createOkResponse(final UpdatedAccounts updatedAccounts) {
        final String msg = createOkMessage(updatedAccounts);
        LOGGER.info(msg);
        return Response.status(OK).entity(new Message(msg).asJson()).build();
    }

    private static String createOkMessage(final UpdatedAccounts updatedAccounts) {
        final Account sourceAccount = updatedAccounts.getSourceAccount();
        final Account destinationAccount = updatedAccounts.getDestinationAccount();
        return String.format("Money transferred - balance for account %s is %.2f and for account %s is %.2f",
                sourceAccount.getId(),
                sourceAccount.getBalance(),
                destinationAccount.getId(),
                destinationAccount.getBalance());
    }

    private static Response createBadRequestResponse(final String msg) {
        LOGGER.error(msg);
        return Response.status(BAD_REQUEST).entity(new Message(msg).asJson()).build();
    }

    private static class Message {
        @JsonProperty
        final String message;

        private Message(final String message) {
            this.message = message;
        }

        private String asJson() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            }
            catch(final JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
