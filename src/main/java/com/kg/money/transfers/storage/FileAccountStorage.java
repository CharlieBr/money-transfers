package com.kg.money.transfers.storage;

import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import com.kg.money.transfers.model.Account;

public class FileAccountStorage implements AccountStorage {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, Lock> accountLocks = new ConcurrentHashMap<>();

    public FileAccountStorage(final String filename) {
        this.accounts.putAll(AccountStorageFileParser.parseAccountsFromFile(filename)
                .stream()
                .collect(toMap(Account::getId, Function.identity())));
        this.accountLocks.putAll(this.accounts.keySet().stream().collect(toMap(id -> id, id -> new ReentrantLock())));
    }

    @Override
    public boolean exists(final String id) {
        return this.accounts.containsKey(id);
    }

    public void transfer(final String sourceAccountId, final String destinationAccountId, final BigDecimal amount) {
        final Lock sourceAccountLock = this.accountLocks.get(sourceAccountId);
        sourceAccountLock.lock();
        final Account sourceAccount = this.accounts.get(sourceAccountId);
        final BigDecimal balance = sourceAccount.getBalance();
        if(balance.compareTo(amount) < 0) {
            sourceAccountLock.unlock();
            throw new IllegalArgumentException("Balance is too low!");
        }
        final Lock destinationAccountLock = this.accountLocks.get(destinationAccountId);
        destinationAccountLock.lock();
        final Account destinationAccount = this.accounts.get(destinationAccountId);
        final Account updatedDestinationAccount = destinationAccount.topUp(amount);
        final Account updatedSourceAccount = sourceAccount.withdraw(amount);
        updateAccount(updatedSourceAccount);
        updateAccount(updatedDestinationAccount);
        sourceAccountLock.unlock();
        destinationAccountLock.unlock();
    }

    @Override
    public void updateAccount(final Account account) {
        this.accounts.put(account.getId(), account);
    }
}
