package com.kg.money.transfers.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class FileAccountStorageTest {
    private static final BigDecimal INITIAL_TOTAL_BALANCE = BigDecimal.valueOf(117.88);
    private static final String ID_1 = "id-1";
    private static final String ID_2 = "id-2";
    private static final String ID_3 = "id-3";

    private final FileAccountStorage storage = new FileAccountStorage("test-accounts.json");
    private final ExecutorService executorService = Executors.newFixedThreadPool(200);

    @Test
    public void shouldThrowExceptionWhenBalanceTooLowForTransfer() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> this.storage.transfer(ID_1, ID_2, BigDecimal.valueOf(100)))
                .withMessage("Balance is too low!");
    }

    @Test
    public void shouldCorrectlyTransferMoneyBetweenTwoAccounts() {
        assertThat(this.storage.getBalanceForAccount(ID_1)).isEqualTo(BigDecimal.valueOf(45.90));
        assertThat(this.storage.getBalanceForAccount(ID_2)).isEqualTo(BigDecimal.valueOf(67.98));
        this.storage.transfer(ID_1, ID_2, BigDecimal.TEN);
        assertThat(this.storage.getBalanceForAccount(ID_1)).isEqualTo(BigDecimal.valueOf(35.90));
        assertThat(this.storage.getBalanceForAccount(ID_2)).isEqualTo(BigDecimal.valueOf(77.98));
    }

    @Test
    public void shouldHandleLotsOfThreads() throws Exception {
        for(int i = 0; i < 1000; i++) {
            final int randomIdx = new Random().nextInt(3);
            final String sourceAccountId = "id-" + randomIdx;
            final String destinationAccountId = "id-" + getDestinationIdx(randomIdx);
            this.executorService.submit(() -> transferAndIgnoreException(sourceAccountId, destinationAccountId));
        }
        this.executorService.shutdown();
        this.executorService.awaitTermination(5, TimeUnit.MINUTES);

        final BigDecimal balance1 = this.storage.getBalanceForAccount(ID_1);
        final BigDecimal balance2 = this.storage.getBalanceForAccount(ID_2);
        final BigDecimal balance3 = this.storage.getBalanceForAccount(ID_3);
        final BigDecimal totalBalance = balance1.add(balance2).add(balance3);
        assertThat(totalBalance).isEqualTo(INITIAL_TOTAL_BALANCE);
        assertThat(balance1).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(balance2).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(balance3).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void shouldHandleManyConcurrentTransactionBetweenTwoAccounts() throws InterruptedException {
        this.executorService.submit(() -> {
            for(int i = 0; i < 10; i++) {
                transferAndIgnoreException(ID_1, ID_2);
            }
        });
        this.executorService.submit(() -> {
            for(int i = 0; i < 10; i++) {
                transferAndIgnoreException(ID_2, ID_1);
            }
        });
        this.executorService.shutdown();
        this.executorService.awaitTermination(5, TimeUnit.MINUTES);
        final BigDecimal balance1 = this.storage.getBalanceForAccount(ID_1);
        final BigDecimal balance2 = this.storage.getBalanceForAccount(ID_2);
        final BigDecimal balance3 = this.storage.getBalanceForAccount(ID_3);
        final BigDecimal totalBalance = balance1.add(balance2).add(balance3);
        assertThat(totalBalance).isEqualTo(INITIAL_TOTAL_BALANCE);
        assertThat(balance1).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(balance2).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(balance3).isGreaterThanOrEqualTo(BigDecimal.valueOf(4));
    }

    private static int getDestinationIdx(final int sourceIdx) {
        int destinationIdx = new Random().nextInt(3);
        while(destinationIdx == sourceIdx) {
            destinationIdx = new Random().nextInt(3);
        }
        return destinationIdx;
    }

    private void transferAndIgnoreException(final String sourceAccountId, final String destinationAccountId) {
        try {
            this.storage.transfer(sourceAccountId, destinationAccountId, BigDecimal.ONE);
        }
        catch(final Exception e) {
            // ignored
        }
    }
}