package com.construo.shop.controller;

import com.construo.shop.dto.OrderCreateRequest;
import com.construo.shop.dto.OrderDto;
import com.construo.shop.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<OrderDto> findAll() {
        return orderService.findAll();
    }
}
