package com.madrugas.processor;

import com.madrugas.model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator implements Processor {
    @Override public void process(Exchange exchange) {
        Order order = exchange.getMessage().getBody(Order.class);
        if (order == null || order.id() == null || order.id().isBlank()
                || order.customerId() == null || order.customerId().isBlank()
                || order.total() == null || order.total().signum() <= 0) {
            throw new IllegalArgumentException("id, customerId e total positivo são obrigatórios");
        }
    }
}
