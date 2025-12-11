package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.PaymentDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PaymentService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    List<PaymentDto> getPayments(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("(hasAnyRole('ADMIN', 'MANAGEMENT')) OR (hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#customerId, authentication.principal.id))")
    List<PaymentDto> getPayments(Integer page, Integer size, String sort, String direction, Long customerId);

    @PreAuthorize("(hasAnyRole('ADMIN', 'MANAGEMENT')) OR (hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByPaymentId(#paymentId, authentication.principal.id))")
    PaymentDto getPaymentById(Long paymentId);

    @PreAuthorize("(hasAnyRole('ADMIN', 'MANAGEMENT')) OR (hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByOrderId(#paymentDto.orderId, authentication.principal.id))")
    PaymentDto createPayment(PaymentDto paymentDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    PaymentDto updatePayment(PaymentDto paymentDto, Long paymentId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    PaymentDto updatePaymentSpecific(Map<String, Object> map, Long paymentId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    void deletePaymentById(Long paymentId);
}
