package com.kg.money.transfers.storage;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kg.money.transfers.model.Account;

class AccountStorageFileParser {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    public static Set<Account> parseAccountsFromFile(final String filename) {
        try(final InputStream accountsJson = AccountStorageFileParser.class.getClassLoader().getResourceAsStream(filename)) {
            return StreamSupport.stream(OBJECT_MAPPER.readTree(Objects.requireNonNull(accountsJson)).spliterator(), false)
                    .map(node -> new Account(node.path("id").asText(), node.path("value").decimalValue()))
                    .collect(toSet());
        }
        catch(final IOException e) {
            throw new RuntimeException(e);
        }
    }
}