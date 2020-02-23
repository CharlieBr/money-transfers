package com.kg.money.transfers.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

class MoneyTransferRequestValidatorTest {

    @Test
    public void shouldThrowExceptionWhenMissingProperty() {
        final JsonNode node = JsonNodeFactory.instance.objectNode().put("property-1", "123");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MoneyTransferRequestValidator.validateTextProperty(node, "other-property"))
                .withMessage("Missing property 'other-property'!");
    }

    @Test
    public void shouldThrowExceptionWhenInvalidProperty() {
        final JsonNode node = JsonNodeFactory.instance.objectNode()
                .put("text-property", "text")
                .put("numeric-property", 123)
                .put("blank-text-property", " ")
                .put("zero", 0)
                .put("less-than-zero", -90.02);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MoneyTransferRequestValidator.validateTextProperty(node, "numeric-property"))
                .withMessage("Invalid property 'numeric-property'. Should be text value!");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MoneyTransferRequestValidator.validateTextProperty(node, "blank-text-property"))
                .withMessage("Invalid property 'blank-text-property'. Cannot be blank!");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MoneyTransferRequestValidator.validateNumericProperty(node, "text-property"))
                .withMessage("Invalid property 'text-property'. Should be numeric value!");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MoneyTransferRequestValidator.validateNumericProperty(node, "zero"))
                .withMessage("Invalid property 'zero'. Should be larger than 0!");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MoneyTransferRequestValidator.validateNumericProperty(node, "less-than-zero"))
                .withMessage("Invalid property 'less-than-zero'. Should be larger than 0!");
    }

    @Test
    public void shouldReturnPropertyCorrectly() {
        final JsonNode node = JsonNodeFactory.instance.objectNode()
                .put("text-property", "text")
                .put("numeric-property", 123.9);
        assertThat(MoneyTransferRequestValidator.validateNumericProperty(node, "numeric-property"))
                .isEqualTo(BigDecimal.valueOf(123.9));
        assertThat(MoneyTransferRequestValidator.validateTextProperty(node, "text-property")).isEqualTo("text");
    }
}