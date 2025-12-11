package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.OrderDto;
import com.karanburadkar.bookstore.entity.type.ShippingStatus;
import com.karanburadkar.bookstore.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;

    public OrderController(HttpServletRequest request, OrderService orderService) {
        super(request);
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrders(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "dir", defaultValue = "asc") String direction,

            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "status", required = false) ShippingStatus status
    ) {
        if (customerId != null) {
            List<OrderDto> orders = orderService.getOrderByCustomerId(customerId, page, size, sort, direction);
            return ResponseEntity.ok(buildApiResponse("Orders fetched for customerId: " + customerId, orders));
        }

        List<OrderDto> orders = orderService.getOrders(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Orders fetched successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable("id") Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(buildApiResponse("Order details fetched for ID: " + orderId, order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@RequestBody @Valid OrderDto orderDto) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New order created successfully", createdOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @RequestBody @Valid OrderDto orderDto, @PathVariable("id") Long orderId) {
        OrderDto updatedOrder = orderService.updateOrder(orderDto, orderId);
        return ResponseEntity.ok(buildApiResponse("Order updated successfully for ID: " + orderId, updatedOrder));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderSpecific(
            @RequestBody Map<String, Object> orderMap, @PathVariable("id") Long orderId) {
        OrderDto updatedOrder = orderService.updateOrderSpecific(orderMap, orderId);
        return ResponseEntity.ok(buildApiResponse("Order partially updated for ID: " + orderId, updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrderById(@PathVariable("id") Long orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok(buildApiResponse("Order deleted successfully"));
    }
}
