package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.address " +
            "LEFT JOIN FETCH o.customer c " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.book " +
            "WHERE c.id = ?1",
            countQuery = "SELECT COUNT(o) FROM Order o")
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    @Query(value = "SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.address " +
            "LEFT JOIN FETCH o.customer " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.book",
            countQuery = "SELECT COUNT(o) FROM Order o")
    Page<Order> findOrdersWithAddressAndCustomerDetails(Pageable pageable);

    @Query(value = "SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.address " +
            "LEFT JOIN FETCH o.customer " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.book " +
            "WHERE o.id = ?1",
            countQuery = "SELECT COUNT(o) FROM Order o")
    Optional<Order> findOrderWithAddressAndCustomerDetailsById(Long orderId);

    @Query("SELECT o.customer.user.id FROM Order o WHERE o.id = ?1")
    Long validateUser(Long orderId);
}
