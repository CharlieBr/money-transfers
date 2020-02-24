package com.kg.money.transfers.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    public void shouldCorrectlyWithdrawMoneyFromAccount() {
        final Account account = new Account("id-1", BigDecimal.TEN);
        assertThat(account.withdraw(BigDecimal.ONE)).isEqualTo(new Account("id-1", BigDecimal.valueOf(9)));
    }

    @Test
    public void shouldCorrectlyTopUpAccount() {
        final Account account = new Account("id-1", BigDecimal.valueOf(9));
        assertThat(account.topUp(BigDecimal.ONE)).isEqualTo(new Account("id-1", BigDecimal.TEN));
    }

}