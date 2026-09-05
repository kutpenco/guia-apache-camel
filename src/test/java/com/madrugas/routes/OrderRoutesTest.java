package com.madrugas.routes;

import com.madrugas.Application;
import com.madrugas.model.Order;
import java.math.BigDecimal;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "camel.springboot.main-run-controller=false")
class OrderRoutesTest {
    @Autowired ProducerTemplate template;
    @Test void deveCriarEPersistirPedido() {
        Order order = new Order("p-1", "c-1", new BigDecimal("125.50"), "BR", null);
        Order saved = template.requestBody("direct:create-order", order, Order.class);
        assertEquals("PROCESSING", saved.status());
        assertEquals(1, template.requestBody("direct:list-orders", null, java.util.Collection.class).size());
    }
    @Test void deveRejeitarPedidoInvalido() {
        Order invalid = new Order("", "c-1", BigDecimal.ZERO, "BR", null);
        String response = template.requestBody("direct:create-order", invalid, String.class);
        assertTrue(response.contains("obrigatórios"));
    }
}
