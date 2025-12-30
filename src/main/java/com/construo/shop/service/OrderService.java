package com.construo.shop.service;

import com.construo.shop.domain.Order;
import com.construo.shop.domain.OrderItem;
import com.construo.shop.domain.Product;
import com.construo.shop.dto.OrderCreateRequest;
import com.construo.shop.dto.OrderDto;
import com.construo.shop.dto.OrderItemDto;
import com.construo.shop.dto.OrderItemRequest;
import com.construo.shop.exception.BadRequestException;
import com.construo.shop.exception.ResourceNotFoundException;
import com.construo.shop.repository.OrderItemRepository;
import com.construo.shop.repository.OrderRepository;
import com.construo.shop.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final String DEFAULT_STATUS = "CREATED";

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public OrderDto create(OrderCreateRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new BadRequestException("Order must include at least one item");
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(DEFAULT_STATUS);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + itemRequest.productId()
                    ));
            BigDecimal quantity = BigDecimal.valueOf(itemRequest.quantity());
            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(quantity);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);
            items.add(item);
            total = total.add(subtotal);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);
        items.forEach(item -> item.setOrder(savedOrder));
        List<OrderItem> savedItems = orderItemRepository.saveAll(items);

        return toDto(savedOrder, savedItems);
    }

    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        Order order = getOrder(id);
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        return toDto(order, items);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            result.add(toDto(order, items));
        }
        return result;
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDto toDto(Order order, List<OrderItem> items) {
        List<OrderItemDto> itemDtos = items.stream()
                .map(this::toDto)
                .toList();
        return new OrderDto(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemDtos
        );
    }

    private OrderItemDto toDto(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
