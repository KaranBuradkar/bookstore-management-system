package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.dto.ReviewResponseDto;
import com.karanburadkar.bookstore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r book FROM Review r LEFT JOIN r.customer c LEFT JOIN r.book b")
    Page<Review> findReviewsDetails(Pageable pageable);

    @Query("SELECT r book FROM Review r LEFT JOIN r.customer c LEFT JOIN r.book b WHERE b.id = ?1")
    Page<Review> findReviewsDetails(Long bookId, Pageable pageable);

    @Query(value = "SELECT new com.karanburadkar.bookstore.dto.ReviewResponseDto(r.id, b.id, c.id, r.comment, r.rating) " +
            "FROM Review r " +
            "LEFT JOIN r.customer c " +
            "LEFT JOIN r.book b " +
            "WHERE r.id = ?1")
    Optional<ReviewResponseDto> findDtoById(Long reviewId);

    @Query(value = "SELECT new com.karanburadkar.bookstore.dto.ReviewResponseDto(r.id, b.id, c.id, r.comment, r.rating) " +
            "FROM Review r " +
            "LEFT JOIN r.customer c " +
            "LEFT JOIN r.book b ")
    List<ReviewResponseDto> findAllDto();

    boolean existsByCustomerIdAndBookId(Long customerId, Long bookId);

    @Query("SELECT r.customer.user.id FROM Review r WHERE r.id = ?1")
    Long validateUser(Long reviewId);
}
