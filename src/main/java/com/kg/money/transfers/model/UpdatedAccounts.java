package com.kg.money.transfers.model;

public class UpdatedAccounts {
    private final Account sourceAccount;
    private final Account destinationAccount;

    public UpdatedAccounts(final Account sourceAccount, final Account destinationAccount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    public Account getSourceAccount() {
        return this.sourceAccount;
    }

    public Account getDestinationAccount() {
        return this.destinationAccount;
    }
}
