package com.kg.money.transfers.storage;

import java.util.Optional;

import com.kg.money.transfers.model.Account;

public class FileAccountStorage implements AccountStorage {

    @Override
    public Optional<Account> getById(final String id) {
        return Optional.empty();
    }
}
