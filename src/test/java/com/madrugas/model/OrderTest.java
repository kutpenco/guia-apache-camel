package com.madrugas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void deveCriarPedidoComNovoStatusSemAlterarDados() {
        // Arrange
        Order order = new Order("p-1", "c-1", new BigDecimal("10.00"), "BR", null);

        // Act
        Order updated = order.withStatus("PROCESSING");

        // Assert
        assertEquals(new Order("p-1", "c-1", new BigDecimal("10.00"), "BR", "PROCESSING"), updated);
        assertEquals(null, order.status());
    }
}
