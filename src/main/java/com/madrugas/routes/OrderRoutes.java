package com.madrugas.routes;

import com.madrugas.model.Order;
import com.madrugas.processor.OrderValidator;
import com.madrugas.service.OrderRepository;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class OrderRoutes extends RouteBuilder {
    private final OrderValidator validator;
    private final OrderRepository repository;
    public OrderRoutes(OrderValidator validator, OrderRepository repository) {
        this.validator = validator; this.repository = repository;
    }
    @Override public void configure() {
        errorHandler(deadLetterChannel("direct:dead-letter").maximumRedeliveries(2)
                .redeliveryDelay(100).retryAttemptedLogLevel(LoggingLevel.WARN));
        onException(IllegalArgumentException.class).handled(true).setHeader("CamelHttpResponseCode", constant(400))
                .setBody(simple("{\"error\":\"${exception.message}\"}"));

        restConfiguration().bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint", "true");
        rest("/api/orders").get().to("direct:rest-list-orders")
            .post().type(Order.class).to("direct:rest-create-order");
        rest("/api/orders/{id}").get().to("direct:rest-get-order");

        from("direct:rest-create-order").routeId("rest-create-order").to("direct:create-order");
        from("direct:rest-list-orders").routeId("rest-list-orders").to("direct:list-orders");
        from("direct:rest-get-order").routeId("rest-get-order").to("direct:get-order");
        from("direct:create-order").routeId("create-order")
            .process(validator).to("direct:save-order").setHeader("CamelHttpResponseCode", constant(201));
        from("direct:save-order").routeId("save-order")
            .process(e -> e.getMessage().setBody(repository.save(e.getMessage().getBody(Order.class).withStatus("PROCESSING"))));
        from("direct:list-orders").routeId("list-orders")
            .process(e -> e.getMessage().setBody(repository.findAll()));
        from("direct:get-order").routeId("get-order")
            .process(e -> {
                Order order = repository.find(e.getMessage().getHeader("id", String.class));
                if (order == null) throw new IllegalArgumentException("Pedido não encontrado");
                e.getMessage().setBody(order);
            });
        from("direct:dead-letter").routeId("dead-letter")
            .log(LoggingLevel.ERROR, "Mensagem enviada para DLQ: ${exception.message}");
    }
}
