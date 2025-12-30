package com.construo.shop.dto;

import java.time.LocalDateTime;

public record CategoryDto(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt
) {}
