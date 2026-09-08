package com.madrugas.routes;

import com.madrugas.Application;
import com.madrugas.model.Order;
import java.math.BigDecimal;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@CamelSpringBootTest
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "camel.springboot.main-run-controller=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderRoutesTest {
    @Autowired ProducerTemplate template;

    @Test
    void deveCriarEPersistirPedido() {
        // Arrange
        Order order = new Order("p-1", "c-1", new BigDecimal("125.50"), "BR", null);

        // Act
        Order saved = template.requestBody("direct:create-order", order, Order.class);

        // Assert
        Assertions.assertEquals("PROCESSING", saved.status());
        java.util.Collection<?> orders =
                template.requestBody("direct:list-orders", null, java.util.Collection.class);
        Assertions.assertEquals(1, orders.size());
    }

    @Test
    void deveRejeitarPedidoInvalido() {
        // Arrange
        Order invalid = new Order("", "c-1", BigDecimal.ZERO, "BR", null);

        // Act
        String response = template.requestBody("direct:create-order", invalid, String.class);

        // Assert
        Assertions.assertTrue(response.contains("obrigatórios"));
    }

    @Test
    void deveConsultarPedidoExistente() {
        // Arrange
        Order order = new Order("p-2", "c-2", new BigDecimal("50.00"), "BR", null);
        template.requestBody("direct:create-order", order, Order.class);

        // Act
        Order found = template.requestBodyAndHeader("direct:get-order", null, "id", "p-2", Order.class);

        // Assert
        Assertions.assertEquals(order.id(), found.id());
        Assertions.assertEquals("PROCESSING", found.status());
    }

    @Test
    void deveInformarQuandoPedidoNaoExiste() {
        // Arrange
        String orderId = "inexistente";

        // Act
        String response = template.requestBodyAndHeader("direct:get-order", null, "id", orderId, String.class);

        // Assert
        Assertions.assertTrue(response.contains("Pedido não encontrado"));
    }
}
