package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi " +
            "LEFT JOIN FETCH oi.book " +
            "WHERE oi.order.id = ?1")
    Set<OrderItem> findByOrderId(Long orderId);


    @Query(value = "SELECT DISTINCT oi FROM OrderItem oi " +
            "LEFT JOIN FETCH oi.book " +
            "LEFT JOIN FETCH oi.order o " +
            "LEFT JOIN FETCH o.customer " +
            "LEFT JOIN FETCH o.address " +
            "WHERE o.id IN :orderIds",
            countQuery = "SELECT COUNT(oi) FROM OrderItem oi")
    Set<OrderItem> findOrderItemsByOrderIds(@Param("orderIds") List<Long> orderIds);
}
