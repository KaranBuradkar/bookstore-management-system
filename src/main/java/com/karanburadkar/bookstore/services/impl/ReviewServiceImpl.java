package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.ReviewAddRequestDto;
import com.karanburadkar.bookstore.dto.ReviewResponseDto;
import com.karanburadkar.bookstore.entity.Book;
import com.karanburadkar.bookstore.entity.Customer;
import com.karanburadkar.bookstore.entity.Review;
import com.karanburadkar.bookstore.exception.database.BookNotFoundException;
import com.karanburadkar.bookstore.exception.database.CustomerNotFoundException;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.mapper.ReviewMapper;
import com.karanburadkar.bookstore.repository.BookRepository;
import com.karanburadkar.bookstore.repository.CustomerRepository;
import com.karanburadkar.bookstore.repository.ReviewRepository;
import com.karanburadkar.bookstore.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, CustomerRepository customerRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<ReviewResponseDto> getReviews(Integer page, Integer size, String sort, String direction, Long bookId) {

        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC: Sort.Direction.DESC;

        Page<Review> reviews = null;

        if (bookId != null) {
            reviews = reviewRepository.findReviewsDetails(bookId, PageRequest.of(page, size, dir, sort));
        }
        else reviews = reviewRepository.findReviewsDetails(PageRequest.of(page, size, dir, sort));

        return reviews.stream().map(review -> {
            ReviewResponseDto dto = reviewMapper.toResponseDto(review);
            dto.setCustomerId(review.getCustomer().getId());
            dto.setBookId(review.getBook().getId());

            return dto;
        }).toList();
    }

    @Override
    public ReviewResponseDto getReviewById(Long reviewId) {
        return reviewRepository.findDtoById(reviewId)
                .orElseThrow(() -> {
                    String message = "Invalid reviewId provided to find Review";
                    log.error(message);
                    return new EntityNotFoundException(message + ", reviewId: " + reviewId);
                });
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewAddRequestDto reviewAddRequestDto) {

        oneCustomerOneReviewPerBook(reviewAddRequestDto.getBookId(), reviewAddRequestDto.getCustomerId());

        // 1. Import required entities
        Book book = resolveBook(reviewAddRequestDto.getBookId());
        Customer customer = resolveCustomer(reviewAddRequestDto.getCustomerId());

        // 2. map dto -> Review entity
        Review review = reviewMapper.toEntity(reviewAddRequestDto);
        review.setBook(book);
        review.setCustomer(customer);

        // 3. saved in DB
        Review savedReview = reviewRepository.save(review);

        // 4. create response Dto
        return reviewMapper.toResponseDto(savedReview);
    }

    private Customer resolveCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer does not exit.");
                    return new CustomerNotFoundException(customerId);
                });
    }

    private void oneCustomerOneReviewPerBook(Long bookId, Long customerId) {
        if (reviewRepository.existsByCustomerIdAndBookId(customerId, bookId)) {
            log.warn("Customer {} already reviewed Book {}", customerId, bookId);
            throw new IllegalArgumentException("Customer has already submitted a review for this book");
        }
    }


    @Override
    @Transactional
    public ReviewResponseDto updateReviewSpecific(Map<String, Object> map, Long reviewId) {
        Review review = resolveReview(reviewId);

        map.forEach((key, value) -> {
            switch (key) {
                case "comment" -> review.setComment((String) value);
                case "rating" -> review.setRating((Integer) value);
                case "customerId" -> {
                    Long customerId = (Long) value;
                    Customer customer = resolveCustomer(customerId);
                    review.setCustomer(customer);
                }
                case "bookId" -> {
                    Long bookId = (Long) value;
                    Book book = resolveBook(bookId);
                    review.setBook(book);
                }

            }
        });

        Review updated = reviewRepository.save(review);

        // 4. create response Dto
        return reviewMapper.toResponseDto(updated);
    }

    private Review resolveReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review not found with Id: {}", reviewId);
                    return new EntityNotFoundException(this.getClass().getName(), reviewId);
                });
    }

    private Book resolveBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.error("Book not found with Id: {}", bookId);
                    return new BookNotFoundException("Book not found with Id: " + bookId);
                });
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(ReviewAddRequestDto reviewAddRequestDto, Long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            String message = "Invalid reviewId provided for update Review";
            log.error(message);
            throw new EntityNotFoundException(getClass().getName(), reviewId);
        }

        // 1. Import required entities
        Book book = resolveBook(reviewAddRequestDto.getBookId());
        Customer customer = resolveCustomer(reviewAddRequestDto.getCustomerId());

        // 2. map dto -> Review entity
        Review review = reviewMapper.toEntity(reviewAddRequestDto);
        review.setId(reviewId);
        review.setBook(book);
        review.setCustomer(customer);

        // 3. saved in DB
        Review savedReview = reviewRepository.save(review);

        // 4. create response Dto
        return reviewMapper.toResponseDto(savedReview);
    }

    @Override
    @Transactional
    public void deleteReviewById(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) reviewRepository.deleteById(reviewId);
        else {
            String message = "Invalid reviewId provided for deletion of Review";
            log.error(message);
            throw new EntityNotFoundException(message+", reviewId: "+reviewId);
        }
        log.info("Review with Id {} deleted successfully.", reviewId);
    }
}
