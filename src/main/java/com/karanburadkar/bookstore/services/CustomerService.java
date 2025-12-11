package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.CustomerRequestDto;
import com.karanburadkar.bookstore.dto.CustomerResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CustomerService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "OR @permissionGuard.isOwnerByCustomerId(#customerId, authentication.principal.id)")
    CustomerResponseDto getCustomerById(Long customerId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    List<CustomerResponseDto> getCustomers(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "OR @permissionGuard.isOwnerByCustomerId(#customerId, authentication.principal.id)")
    CustomerResponseDto updateCustomerById(CustomerRequestDto customerRequestDto, Long customerId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "OR @permissionGuard.isOwnerByCustomerId(#customerId, authentication.principal.id)")
    CustomerResponseDto updateCustomerSpecifics(Map<String, Object> map, Long customerId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "OR @permissionGuard.isOwnerByCustomerId(#customerId, authentication.principal.id)")
    void deleteCustomerById(Long customerId);
}
