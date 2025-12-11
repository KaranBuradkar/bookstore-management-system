package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT p FROM Payment p " +
            "LEFT JOIN FETCH p.order o " +
            "LEFT JOIN FETCH p.paymentMethod pm ",
    countQuery = "SELECT COUNT(p) FROM Payment p")
    Page<Payment> findAllWithDetails(Pageable pageable);

    @Query(value = "SELECT p FROM Payment p " +
            "LEFT JOIN FETCH p.order o " +
            "LEFT JOIN FETCH p.paymentMethod pm " +
            "WHERE o.customer.id = ?1",
            countQuery = "SELECT COUNT(p) FROM Payment p")
    Page<Payment> findAllWithDetails(Long customerId, Pageable pageable);

    @Query(value = "SELECT p FROM Payment p " +
            "LEFT JOIN FETCH p.order o " +
            "LEFT JOIN FETCH p.paymentMethod pm " +
            "WHERE p.id = ?1",
            countQuery = "SELECT COUNT(p) FROM Payment p")
    Optional<Payment> findPaymentWithDetailsById(Long paymentId);

    Optional<Payment> findByOrderId(Long orderId);

    @Query("SELECT p.order.customer.user.id FROM Payment p WHERE p.id = ?1 ")
    Long validateUser(Long paymentId);
}
