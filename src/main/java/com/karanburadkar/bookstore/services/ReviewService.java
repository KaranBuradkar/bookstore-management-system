package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.ReviewAddRequestDto;
import com.karanburadkar.bookstore.dto.ReviewResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    List<ReviewResponseDto> getReviews(Integer page, Integer size, String sort, String direction, Long bookId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    ReviewResponseDto getReviewById(Long reviewId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#reviewAddRequestDto.customerId, authentication.principal.id)")
    ReviewResponseDto createReview(ReviewAddRequestDto reviewAddRequestDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByReviewId(#reviewId, authentication.principal.id)")
    ReviewResponseDto updateReviewSpecific(Map<String, Object> map, Long reviewId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByCustomerId(#reviewAddRequestDto.customerId, authentication.principal.id) " +
            "AND @permissionGuard.isOwnerByReviewId(#reviewId, authentication.principal.id)")
    ReviewResponseDto updateReview(ReviewAddRequestDto reviewAddRequestDto, Long reviewId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('CUSTOMER') " +
            "AND @permissionGuard.isOwnerByReviewId(#reviewId, authentication.principal.id)")
    void deleteReviewById(Long reviewId);
}
