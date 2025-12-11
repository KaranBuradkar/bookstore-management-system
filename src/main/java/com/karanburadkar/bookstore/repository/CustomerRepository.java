package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.user WHERE c.id = ?1")
    Optional<Customer> findCustomerById(Long customerId);

    Optional<Customer> findByUserId(Long id);

    @Query("SELECT c.user.id FROM Customer c WHERE c.id = ?1")
    Long validateUser(Long customerId);
}
