package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.PaymentDto;
import com.karanburadkar.bookstore.dto.PaymentMethodDto;
import com.karanburadkar.bookstore.entity.Order;
import com.karanburadkar.bookstore.entity.Payment;
import com.karanburadkar.bookstore.entity.PaymentMethod;
import com.karanburadkar.bookstore.entity.type.PaymentStatus;
import com.karanburadkar.bookstore.exception.business.PaymentAlreadyDoneException;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.exception.database.OrderNotFoundException;
import com.karanburadkar.bookstore.mapper.PaymentMapper;
import com.karanburadkar.bookstore.repository.OrderRepository;
import com.karanburadkar.bookstore.repository.PaymentMethodRepository;
import com.karanburadkar.bookstore.repository.PaymentRepository;
import com.karanburadkar.bookstore.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository, PaymentMethodRepository paymentMethodRepository, PaymentMapper paymentMapper) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public List<PaymentDto> getPayments(Integer page, Integer size, String sort, String direction) {
        Sort.Direction dir = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Payment> paymentPage = paymentRepository.findAllWithDetails(PageRequest.of(page, size, dir, sort));

        return paymentPage.stream()
                .map(p -> {
                    PaymentDto dto = paymentMapper.toDto(p);
                    dto.setOrderId(p.getOrder().getId());
                    dto.setPaymentMethod(
                            new PaymentMethodDto(p.getPaymentMethod().getId(), p.getPaymentMethod().getName())
                    );

                    return dto;
                }).toList();
    }

    @Override
    public List<PaymentDto> getPayments(Integer page, Integer size, String sort, String direction, Long customerId) {
        Sort.Direction dir = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Payment> paymentPage = paymentRepository.findAllWithDetails(customerId, PageRequest.of(page, size, dir, sort));

        return paymentPage.stream()
                .map(p -> {
                    PaymentDto dto = paymentMapper.toDto(p);
                    dto.setOrderId(p.getOrder().getId());
                    dto.setPaymentMethod(
                            new PaymentMethodDto(p.getPaymentMethod().getId(), p.getPaymentMethod().getName())
                    );

                    return dto;
                }).toList();
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findPaymentWithDetailsById(paymentId)
                .orElseThrow(() -> {
                    String message = "Payment entry not found";
                    log.error(message);
                    return new EntityNotFoundException(message + " with paymentId: " + paymentId);
                });

        PaymentDto paymentDto = paymentMapper.toDto(payment);
        paymentDto.setOrderId(payment.getOrder().getId());
        paymentDto.setPaymentMethod(
                new PaymentMethodDto(
                        payment.getPaymentMethod().getId(),
                        payment.getPaymentMethod().getName()
                )
        );

        return paymentDto;
    }

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto paymentDto) {
        if (paymentDto.getId() != null) {
            String message = "creating payment id provided";
            log.error(message);
            throw new IllegalArgumentException(message+" id: "+paymentDto.getId());
        }

        isPaymentPending(paymentDto.getOrderId());

        // 1. Get Order
        Order order = resolveOrder(paymentDto.getOrderId());

        BigDecimal totalAmountExpected = order.getTotalAmount();
        if (!totalAmountExpected.equals(paymentDto.getAmount())) {
            String message = "Payment expected amount is "+totalAmountExpected+" but provided amount is "+paymentDto.getAmount();
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(paymentDto.getPaymentMethod().getName())
                .orElseThrow(() -> {
                    String message = "Payment method not found with name: ";
                    log.error("{}{}", message, paymentDto.getPaymentMethod().getName());
                    return new EntityNotFoundException(message + paymentDto.getPaymentMethod().getName());
                });

        Payment payment = paymentMapper.toEntity(paymentDto);
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        Payment savedPayment = paymentRepository.save(payment);

        PaymentDto response = paymentMapper.toDto(savedPayment);
        response.setOrderId(savedPayment.getOrder().getId());
        response.setPaymentMethod(
                new PaymentMethodDto(
                        savedPayment.getPaymentMethod().getId(),
                        savedPayment.getPaymentMethod().getName()
                )
        );

        return response;
    }

    private void isPaymentPending(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElse(null);
        if (payment != null) {
            throw new PaymentAlreadyDoneException();
        }
    }

    @Override
    @Transactional
    public PaymentDto updatePayment(PaymentDto paymentDto, Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
                String message = "Payment entry not found";
                log.error(message);
                throw  new EntityNotFoundException(message + " with paymentId: " + paymentId);
        }

        // 1. Get Order
        Order order = resolveOrder(paymentDto.getOrderId());

        BigDecimal totalAmountExpected = order.getTotalAmount();
        if (!totalAmountExpected.equals(paymentDto.getAmount())) {
            String message = "Payment expected amount is "+totalAmountExpected+" but provided amount is "+paymentDto.getAmount();
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(paymentDto.getPaymentMethod().getName())
                .orElseThrow(() -> {
                    String message = "Payment method not found with name: ";
                    log.error("{}{}", message, paymentDto.getPaymentMethod().getName());
                    return new EntityNotFoundException(message + paymentDto.getPaymentMethod().getName());
                });

        // map dto -> payment entity
        Payment payment = paymentMapper.toEntity(paymentDto);
        payment.setId(paymentId);
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        // update in DB
        Payment updatedPayment = paymentRepository.save(payment);

        // map entity -> updatedPayment dto
        PaymentDto response = paymentMapper.toDto(updatedPayment);
        response.setOrderId(updatedPayment.getOrder().getId());
        response.setPaymentMethod(
                PaymentMethodDto.of(
                        updatedPayment.getPaymentMethod().getId(),
                        updatedPayment.getPaymentMethod().getName()
                )
        );

        return response;
    }

    @Override
    @Transactional
    public PaymentDto updatePaymentSpecific(Map<String, Object> map, Long paymentId) {
        Payment payment = paymentRepository.findPaymentWithDetailsById(paymentId)
                .orElseThrow(() -> {
                    String message = "Payment entry not found";
                    log.error(message);
                    return new EntityNotFoundException(message + " with paymentId: " + paymentId);
                });
        map.forEach((key, value) -> {
            switch (key) {
                case "status" -> {
                    PaymentStatus status = PaymentStatus.valueOf((String) value);
                    payment.setStatus(status);
                }
                case "amount" -> {
                    BigDecimal amount = (BigDecimal) value;
                    if (amount.scale() != 2) {
                        String message = "Payment amount must have 2 decimal.";
                        log.error(message);
                        throw new IllegalArgumentException(message);
                    }
                    payment.setAmount(amount);
                }
                case "date" -> payment.setDate((LocalDate) value);
                case "paymentMethodDto", "paymentMethod" -> {
                    PaymentMethodDto pmDto = (PaymentMethodDto) value;
                    payment.setPaymentMethod(PaymentMethod.of(pmDto.getId(), pmDto.getName()));
                }
                case "orderId" -> {
                    Long orderId = (Long) value;
                    Order order = resolveOrder(orderId);
                    payment.setOrder(order);
                }
                default -> {
                    String message = "Invalid key provided to update Payment detail, key: "+key;
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }

            }
        });

        Payment updated = paymentRepository.save(payment);

        // map entity -> updatedPayment dto
        PaymentDto response = paymentMapper.toDto(updated);
        response.setOrderId(updated.getOrder().getId());
        response.setPaymentMethod(PaymentMethodDto.of(
                        updated.getPaymentMethod().getId(),
                        updated.getPaymentMethod().getName()
                )
        );

        return response;
    }

    private Order resolveOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found.");
                    return new OrderNotFoundException("Order not found with Id: " + orderId);
                });
    }

    @Override
    @Transactional
    public void deletePaymentById(Long paymentId) {
        if (paymentRepository.existsById(paymentId)) paymentRepository.deleteById(paymentId);
        else {
            String message = "Invalid paymentId provided for delete Payment";
            log.error(message);
            throw new EntityNotFoundException(message+", paymentId: "+paymentId);
        }
        log.info("Payment entry deleted successfully.");
    }
}
