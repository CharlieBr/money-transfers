package com.kg.money.transfers.validators;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;
import com.kg.money.transfers.accounts.Accounts;

public class MoneyTransferRequestValidator {

    public static String validateTextProperty(final JsonNode node, final String name) {
        final JsonNode property = validatePropertyExists(node, name);
        if(!property.isTextual()) {
            throw new IllegalArgumentException(invalidPropertyMessage(name, "Should be text value!"));
        }
        final String value = property.asText();
        if(value.isBlank()) {
            throw new IllegalArgumentException(invalidPropertyMessage(name, "Cannot be blank!"));
        }
        return value;
    }

    public static BigDecimal validateNumericProperty(final JsonNode node, final String name) {
        final JsonNode property = validatePropertyExists(node, name);
        if(!property.isNumber()) {
            throw new IllegalArgumentException(invalidPropertyMessage(name, "Should be numeric value!"));
        }
        final BigDecimal propertyValue = property.decimalValue();
        if(propertyValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(invalidPropertyMessage(name, "Should be larger than 0!"));
        }
        if(propertyValue.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException(invalidPropertyMessage(name,
                    "Should have at most 2 digits to the right of the decimal point!"));
        }
        return propertyValue;
    }

    static JsonNode validatePropertyExists(final JsonNode node, final String name) {
        final JsonNode property = node.path(name);
        if(property.isMissingNode()) {
            throw new IllegalArgumentException(missingPropertyMessage(name));
        }
        return property;
    }

    private static String invalidPropertyMessage(final String name, final String details) {
        return "Invalid property '" + name + "'. " + details;
    }

    private static String missingPropertyMessage(final String name) {
        return "Missing property '" + name + "'!";
    }

    public static String validateExistingAccount(final String id, final Accounts accounts) {
        if(!accounts.exists(id)) {
            throw new IllegalArgumentException("Account with id " + id + " does not exist!");
        }
        return id;
    }

    public static void checkIfDifferentAccounts(final String sourceAccountId, final String destinationAccountId) {
        if(sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Could not transfer account between the same account");
        }
    }
}
