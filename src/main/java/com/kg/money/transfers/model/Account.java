package com.kg.money.transfers.model;

import java.math.BigDecimal;

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
}
