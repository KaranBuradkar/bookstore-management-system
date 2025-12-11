package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.PaymentDto;
import com.karanburadkar.bookstore.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(HttpServletRequest request, PaymentService paymentService) {
        super(request);
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentDto>>> getPayments(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "dir", defaultValue = "asc") String direction,
            @RequestParam(value = "cId", required = false) Long customerId
    ) {
        List<PaymentDto> payments;

        if (customerId != null) {
            payments = paymentService.getPayments(page, size, sort, direction, customerId);
        } else payments = paymentService.getPayments(page, size, sort, direction);

        String message = "Payments fetched successfully";
        if (payments.isEmpty()) message = "any payments till not done";
        return ResponseEntity.ok(buildApiResponse(message, payments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentById(@PathVariable("id") Long paymentId) {
        PaymentDto payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(buildApiResponse("Payment details fetched for ID: " + paymentId, payment));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDto>> createPayment(@RequestBody @Valid PaymentDto paymentDto) {
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New payment created successfully", createdPayment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDto>> updatePayment(
            @RequestBody @Valid PaymentDto paymentDto, @PathVariable("id") Long paymentId) {
        PaymentDto updatedPayment = paymentService.updatePayment(paymentDto, paymentId);
        return ResponseEntity.ok(buildApiResponse("Payment updated successfully for ID: " + paymentId, updatedPayment));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDto>> updatePaymentSpecific(
            @RequestBody Map<String, Object> map, @PathVariable("id") Long paymentId) {
        PaymentDto updatedPayment = paymentService.updatePaymentSpecific(map, paymentId);
        return ResponseEntity.ok(buildApiResponse("Payment partially updated for ID: " + paymentId, updatedPayment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePaymentById(@PathVariable("id") Long paymentId) {
        paymentService.deletePaymentById(paymentId);
        return ResponseEntity.ok(buildApiResponse("Payment detail deleted successfully"));
    }
}
