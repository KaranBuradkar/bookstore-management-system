package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.ReviewAddRequestDto;
import com.karanburadkar.bookstore.dto.ReviewResponseDto;
import com.karanburadkar.bookstore.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/reviews")
public class ReviewController extends BaseController {

    private final ReviewService reviewService;

    public ReviewController(HttpServletRequest request, ReviewService reviewService) {
        super(request);
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviews(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "dir", defaultValue = "asc") String direction,
            @RequestParam(value = "bookId", required = false) Long bookId
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviews(page, size, sort, direction, bookId);
        return ResponseEntity.ok(buildApiResponse("Reviews fetched successfully", reviews));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> getReviewById(@PathVariable("id") Long reviewId) {
        ReviewResponseDto review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(buildApiResponse("Review fetched successfully for ID: " + reviewId, review));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @RequestBody @Valid ReviewAddRequestDto reviewAddRequestDto) {

        ReviewResponseDto createdReview = reviewService.createReview(reviewAddRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("Review created successfully", createdReview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @RequestBody @Valid ReviewAddRequestDto reviewAddRequestDto, @PathVariable("id") Long reviewId) {

        ReviewResponseDto updatedReview = reviewService.updateReview(reviewAddRequestDto, reviewId);
        return ResponseEntity.ok(buildApiResponse("Review updated successfully for ID: " + reviewId, updatedReview));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReviewSpecific(
            @RequestBody Map<String, Object> map, @PathVariable("id") Long reviewId) {

        ReviewResponseDto updatedReview = reviewService.updateReviewSpecific(map, reviewId);
        return ResponseEntity.ok(buildApiResponse("Review partially updated for ID: " + reviewId, updatedReview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReviewById(@PathVariable("id") Long reviewId) {
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.ok(buildApiResponse("Review deleted successfully"));
    }
}
