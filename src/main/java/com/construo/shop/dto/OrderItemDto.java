package com.construo.shop.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
