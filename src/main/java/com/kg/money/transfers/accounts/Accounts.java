package com.kg.money.transfers.accounts;

import java.math.BigDecimal;

import com.kg.money.transfers.model.UpdatedAccounts;

public interface Accounts {

    boolean exists(final String id);

    BigDecimal getBalanceForAccount(final String id);

    UpdatedAccounts transfer(final String sourceAccountId, final String destinationAccountId, final BigDecimal amount);
}
