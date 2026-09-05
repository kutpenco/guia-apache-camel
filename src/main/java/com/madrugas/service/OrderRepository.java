package com.madrugas.service;

import com.madrugas.model.Order;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class OrderRepository {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    public Order save(Order order) { orders.put(order.id(), order); return order; }
    public Collection<Order> findAll() { return orders.values(); }
    public Order find(String id) { return orders.get(id); }
}
