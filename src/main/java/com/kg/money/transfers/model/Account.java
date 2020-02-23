package com.kg.money.transfers.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Account {
    private final String id;
    private final BigDecimal balance;

    public Account(final String id, final BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Account withdraw(final BigDecimal amount) {
        return new Account(this.id, this.balance.subtract(amount));
    }

    public Account topUp(final BigDecimal amount) {
        return new Account(this.id, this.balance.add(amount));
    }

    public String getId() {
        return this.id;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public boolean equals(final Object o) {
        if(o instanceof Account) {
            final Account that = (Account) o;
            return Objects.equals(this.id, that.id) && Objects.equals(this.balance, that.balance);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.balance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("id='" + this.id + "'")
                .add("balance=" + this.balance)
                .toString();
    }
}
