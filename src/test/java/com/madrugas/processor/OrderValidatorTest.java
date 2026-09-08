package com.madrugas.processor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.madrugas.model.Order;
import java.math.BigDecimal;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;

class OrderValidatorTest {
    private final OrderValidator validator = new OrderValidator();

    @Test
    void deveAceitarPedidoValido() {
        // Arrange
        Order order = new Order("p-1", "c-1", new BigDecimal("10.00"), "BR", null);
        DefaultExchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getMessage().setBody(order);

        // Act and Assert
        assertDoesNotThrow(() -> validator.process(exchange));
    }

    @Test
    void deveRejeitarPedidoNulo() {
        // Arrange
        DefaultExchange exchange = new DefaultExchange(new DefaultCamelContext());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> validator.process(exchange));
    }

    @Test
    void deveRejeitarTotalNaoPositivo() {
        // Arrange
        Order order = new Order("p-1", "c-1", BigDecimal.ZERO, "BR", null);
        DefaultExchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getMessage().setBody(order);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> validator.process(exchange));
    }
}
