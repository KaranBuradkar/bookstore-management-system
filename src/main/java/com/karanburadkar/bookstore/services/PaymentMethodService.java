package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.PaymentMethodDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethodDto> getPaymentMethods(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    PaymentMethodDto getPaymentMethodById(Long categoryId);

    @PreAuthorize("hasAnyRole('ADMIN')")
    PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto);

    @PreAuthorize("hasAnyRole('ADMIN')")
    PaymentMethodDto updatePaymentMethod(PaymentMethodDto paymentMethodDto, Long paymentMethodId);

    @PreAuthorize("hasAnyRole('ADMIN')")
    void deletePaymentMethodById(Long paymentMethodId);
}
