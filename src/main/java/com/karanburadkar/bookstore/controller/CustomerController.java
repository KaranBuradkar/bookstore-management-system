package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.CustomerRequestDto;
import com.karanburadkar.bookstore.dto.CustomerResponseDto;
import com.karanburadkar.bookstore.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService, HttpServletRequest request) {
        super(request);
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDto>>> getCustomers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "dir", defaultValue = "ASC") String direction
    ) {
        List<CustomerResponseDto> customers = customerService.getCustomers(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Customers fetched successfully", customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> getCustomerById(@PathVariable("id") Long customerId) {
        CustomerResponseDto customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(buildApiResponse("Customer fetched successfully", customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> updateCustomerById(
            @RequestBody @Valid CustomerRequestDto customerRequestDto,
            @PathVariable("id") Long customerId) {
        CustomerResponseDto updated = customerService.updateCustomerById(customerRequestDto, customerId);
        return ResponseEntity.ok(buildApiResponse("Customer updated successfully", updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> updateCustomerSpecifics(
            @RequestBody Map<String, Object> map,
            @PathVariable("id") Long customerId) {
        CustomerResponseDto updated = customerService.updateCustomerSpecifics(map, customerId);
        return ResponseEntity.ok(buildApiResponse("Customer partially updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerById(@PathVariable("id") Long customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.ok(buildApiResponse("Customer deleted successfully"));
    }
}
