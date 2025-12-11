package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.PaymentMethodDto;
import com.karanburadkar.bookstore.services.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payment-methods")
public class PaymentMethodController extends BaseController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(HttpServletRequest request, PaymentMethodService paymentMethodService) {
        super(request);
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentMethodDto>>> getPaymentMethods(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        List<PaymentMethodDto> methods = paymentMethodService.getPaymentMethods(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Payment methods fetched successfully", methods));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodDto>> getPaymentMethodById(@PathVariable("id") Long methodId) {
        PaymentMethodDto method = paymentMethodService.getPaymentMethodById(methodId);
        return ResponseEntity.ok(buildApiResponse("Payment method fetched successfully for ID: " + methodId, method));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentMethodDto>> createPaymentMethod(
            @RequestBody @Valid PaymentMethodDto paymentMethodDto) {

        PaymentMethodDto created = paymentMethodService.createPaymentMethod(paymentMethodDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New payment method created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodDto>> updatePaymentMethod(
            @RequestBody @Valid PaymentMethodDto paymentMethodDto, @PathVariable("id") Long methodId) {

        PaymentMethodDto updated = paymentMethodService.updatePaymentMethod(paymentMethodDto, methodId);
        return ResponseEntity.ok(buildApiResponse("Payment method updated successfully for ID: " + methodId, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePaymentMethodById(@PathVariable("id") Long methodId) {
        paymentMethodService.deletePaymentMethodById(methodId);
        return ResponseEntity.ok(buildApiResponse("Payment method deleted successfully"));
    }
}
