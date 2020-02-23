package com.kg.money.transfers.validators;

import java.util.function.Predicate;

public class TransferValidator {

    public static String validateExistingAccount(final String id, final Predicate<String> predicate) {
        if(!predicate.test(id)) {
            throw new IllegalArgumentException("Account with id " + id + " does not exist!");
        }
        return id;
    }

    public static void checkIfTransferPossible(final String sourceAccountId, final String destinationAccountId) {
        if(sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Could not transfer account between the same account");
        }
    }
}
