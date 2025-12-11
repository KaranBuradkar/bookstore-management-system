package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.OrderDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'CUSTOMER')")
    List<OrderDto> getOrders(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'CUSTOMER')")
    OrderDto getOrderById(Long orderId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#customerId, authentication.principal.id)")
    List<OrderDto> getOrderByCustomerId(Long customerId, Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR (hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#orderDto.customerId, authentication.principal.id))")
    OrderDto createOrder(OrderDto orderDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    OrderDto updateOrderSpecific(Map<String, Object> orderMap, Long orderId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#orderDto.customerId, authentication.principal.id)")
    OrderDto updateOrder(OrderDto orderDto, Long orderId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#orderDto.customerId, authentication.principal.id)")
    void deleteOrderById(Long orderId);
}
