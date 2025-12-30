package com.construo.shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        String orderNumber,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<OrderItemDto> items
) {}
