package com.kg.money.transfers.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MoneyTransferRequest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String from;
    private final String to;
    private final BigDecimal amount;

    public MoneyTransferRequest(final String from, final String to, final BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getSourceAccountId() {
        return this.from;
    }

    public String getDestinationAccountId() {
        return this.to;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public static MoneyTransferRequest fromJson(final String json) throws JsonProcessingException {
        final JsonNode node = OBJECT_MAPPER.readTree(json);
        final String from = node.path("from").asText();
        final String to = node.path("to").asText();
        final BigDecimal amount = node.path("amount").decimalValue();
        return new MoneyTransferRequest(from, to, amount);
    }
}
