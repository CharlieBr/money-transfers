package com.kg.money.transfers.storage;

import java.util.Optional;
import java.util.Set;

import com.kg.money.transfers.model.Account;

public class FileAccountStorage implements AccountStorage {
    private final Set<Account> accounts;

    public FileAccountStorage(final String filename) {
        this.accounts = AccountStorageFileParser.parseAccountsFromFile(filename);
    }

    @Override
    public Optional<Account> getById(final String id) {
        return Optional.empty();
    }
}
