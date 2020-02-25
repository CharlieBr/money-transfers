package com.kg.money.transfers.accounts;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Stream;

import com.kg.money.transfers.model.Account;
import com.kg.money.transfers.model.UpdatedAccounts;

public class FileAccounts implements Accounts {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, Lock> accountLocks = new ConcurrentHashMap<>();

    public FileAccounts(final String filename) {
        this.accounts.putAll(AccountsFileParser.parseAccountsFromFile(filename)
                .stream()
                .collect(toMap(Account::getId, Function.identity())));
        this.accountLocks.putAll(this.accounts.keySet().stream().collect(toMap(id -> id, id -> new ReentrantLock())));
    }

    @Override
    public boolean exists(final String id) {
        return this.accounts.containsKey(id);
    }

    @Override
    public BigDecimal getBalanceForAccount(final String id) {
        return this.accounts.get(id).getBalance();
    }

    public void updateAccount(final Account account) {
        this.accounts.put(account.getId(), account);
    }

    @Override
    public UpdatedAccounts transfer(final String sourceAccountId,
                                    final String destinationAccountId,
                                    final BigDecimal amount) {
        final List<Lock> locks = getSortedLocksForIds(sourceAccountId, destinationAccountId);
        final Lock first = locks.get(0);
        final Lock second = locks.get(1);
        first.lock();
        second.lock();
        final Account sourceAccount = this.accounts.get(sourceAccountId);
        checkBalance(sourceAccount.getBalance(), amount, first, second);
        final Account destinationAccount = this.accounts.get(destinationAccountId);
        final Account updatedDestinationAccount = destinationAccount.topUp(amount);
        final Account updatedSourceAccount = sourceAccount.withdraw(amount);
        updateAccount(updatedSourceAccount);
        updateAccount(updatedDestinationAccount);
        second.unlock();
        first.unlock();
        return new UpdatedAccounts(updatedSourceAccount, updatedDestinationAccount);
    }

    private void checkBalance(final BigDecimal balance, final BigDecimal amount, final Lock first, final Lock second) {
        if(balance.compareTo(amount) < 0) {
            second.unlock();
            first.unlock();
            throw new IllegalArgumentException(String.format("Balance %.2f of source account is too low!", balance));
        }
    }

    private List<Lock> getSortedLocksForIds(final String id1, final String id2) {
        return Stream.of(id1, id2).sorted().map(this.accountLocks::get).collect(toList());
    }
}
