package com.restaurantsystem.restaurantmanagementapi.controller;

import com.restaurantsystem.restaurantmanagementapi.dto.request.OrderCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.OrderResponse;
import com.restaurantsystem.restaurantmanagementapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> openOrder(
            @PathVariable Long restaurantId,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        OrderResponse response = orderService.openOrder(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable Long restaurantId,
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.findById(restaurantId, orderId));
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<OrderResponse>> findByTable(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId
    ) {
        return ResponseEntity.ok(orderService.findByTable(restaurantId, tableId));
    }
}
