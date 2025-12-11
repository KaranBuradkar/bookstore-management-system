package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query("SELECT p.user.id FROM Publisher p WHERE p.id = ?1")
    Optional<Long> getUserIdById(Long publisherId);

    Optional<Publisher> findByUserId(Long userId);
}
