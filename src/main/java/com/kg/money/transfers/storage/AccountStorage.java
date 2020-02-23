package com.kg.money.transfers.storage;

import java.math.BigDecimal;

import com.kg.money.transfers.model.Account;

public interface AccountStorage {

    boolean exists(final String id);

    void transfer(final String sourceAccountId, final String destinationAccountId, final BigDecimal amount);

    void updateAccount(final Account updatedAccount);
}
