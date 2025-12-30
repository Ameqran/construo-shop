package com.construo.shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDto(
        Long id,
        String name,
        String description,
        Long categoryId,
        BigDecimal price,
        Integer stockQuantity,
        String unit,
        LocalDateTime createdAt
) {}
