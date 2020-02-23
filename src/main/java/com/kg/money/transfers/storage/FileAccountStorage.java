package com.kg.money.transfers.storage;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.kg.money.transfers.model.Account;

public class FileAccountStorage implements AccountStorage {
    private final Map<String, Account> accounts;

    public FileAccountStorage(final String filename) {
        this.accounts = AccountStorageFileParser.parseAccountsFromFile(filename)
                .stream()
                .collect(toMap(Account::getId, Function.identity()));
    }

    @Override
    public Optional<Account> getById(final String id) {
        return Optional.ofNullable(this.accounts.get(id));
    }
}
