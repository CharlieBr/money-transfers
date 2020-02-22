package com.kg.money.transfers.storage;

import java.util.Optional;

import com.kg.money.transfers.model.Account;

public interface AccountStorage {

    Optional<Account> getById(final String id);
}
