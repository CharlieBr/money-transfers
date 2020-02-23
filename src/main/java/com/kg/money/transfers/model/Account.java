package com.kg.money.transfers.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Account {
    private final String id;
    private final BigDecimal value;

    public Account(final String id, final BigDecimal value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if(o instanceof Account) {
            final Account that = (Account) o;
            return Objects.equals(this.id, that.id) && Objects.equals(this.value, that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("id='" + this.id + "'")
                .add("value=" + this.value)
                .toString();
    }
}
