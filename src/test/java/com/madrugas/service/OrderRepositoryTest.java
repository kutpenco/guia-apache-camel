package com.madrugas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.madrugas.model.Order;
import java.math.BigDecimal;
import java.util.Collection;
import org.junit.jupiter.api.Test;

class OrderRepositoryTest {
    private final OrderRepository repository = new OrderRepository();

    @Test
    void deveSalvarEConsultarPedidoPorId() {
        // Arrange
        Order order = new Order("p-1", "c-1", new BigDecimal("10.00"), "BR", "PROCESSING");

        // Act
        Order saved = repository.save(order);
        Order found = repository.find(order.id());

        // Assert
        assertSame(order, saved);
        assertSame(order, found);
    }

    @Test
    void deveListarPedidosSalvos() {
        // Arrange
        Order first = new Order("p-1", "c-1", new BigDecimal("10.00"), "BR", "PROCESSING");
        Order second = new Order("p-2", "c-2", new BigDecimal("20.00"), "BR", "PROCESSING");
        repository.save(first);
        repository.save(second);

        // Act
        Collection<Order> orders = repository.findAll();

        // Assert
        assertEquals(2, orders.size());
        org.junit.jupiter.api.Assertions.assertTrue(orders.contains(first));
        org.junit.jupiter.api.Assertions.assertTrue(orders.contains(second));
    }
}
