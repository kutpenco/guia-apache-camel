package com.madrugas.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record Order(@NotBlank String id, @NotBlank String customerId, @Positive BigDecimal total,
                    String country, String status) {
    public Order withStatus(String value) { return new Order(id, customerId, total, country, value); }
}
