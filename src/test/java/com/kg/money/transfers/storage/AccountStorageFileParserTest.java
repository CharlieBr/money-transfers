package com.kg.money.transfers.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.kg.money.transfers.model.Account;

class AccountStorageFileParserTest {

    @Test
    public void shouldThrowExceptionWhenFileDoesNotExist() {
        assertThrows(RuntimeException.class,
                () -> AccountStorageFileParser.parseAccountsFromFile("non-existing-file.json"));
    }

    @Test
    public void shouldThrowExceptionWhenNoAccountsInFile() {
        assertThrows(RuntimeException.class,
                () -> AccountStorageFileParser.parseAccountsFromFile("empty-file.json"));
        assertThrows(RuntimeException.class,
                () -> AccountStorageFileParser.parseAccountsFromFile("empty-accounts-array.json"));
    }

    @Test
    public void shouldCorrectlyParseAccountsFromFile() {
        final Set<Account> accounts = AccountStorageFileParser.parseAccountsFromFile("test-accounts.json");
        assertThat(accounts).containsExactlyInAnyOrder(new Account("id-1", BigDecimal.valueOf(45.9)),
                new Account("id-2", BigDecimal.valueOf(67.98)),
                new Account("id-3", BigDecimal.valueOf(4.0)));
    }
}