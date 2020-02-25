package com.kg.money.transfers.accounts;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kg.money.transfers.model.Account;

class AccountsFileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsFileParser.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Set<Account> parseAccountsFromFile(final String filename) {
        final Set<JsonNode> accounts = readAccountsFromFile(filename);
        if(accounts.isEmpty()) {
            throw new RuntimeException("No accounts data in file '" + filename + "'");
        }
        return accounts.stream()
                .map(node -> new Account(node.path("id").asText(), node.path("balance").decimalValue()))
                .collect(toSet());
    }

    private static Set<JsonNode> readAccountsFromFile(final String filename) {
        try(final InputStream json = AccountsFileParser.class.getClassLoader().getResourceAsStream(filename)) {
            Objects.requireNonNull(json);
            return StreamSupport.stream(OBJECT_MAPPER.readTree(json).spliterator(), false).collect(toSet());
        }
        catch(final IOException e) {
            LOGGER.error("Problem with reading accounts from file '{}' - {}", filename, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}