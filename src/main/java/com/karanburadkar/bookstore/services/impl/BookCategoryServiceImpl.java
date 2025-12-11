package com.karanburadkar.bookstore.services.impl;

import com.karanburadkar.bookstore.exception.database.DataIntegrityViolationException;
import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.BookCategoryDto;
import com.karanburadkar.bookstore.entity.BookCategory;
import com.karanburadkar.bookstore.exception.database.EntityNotFoundException;
import com.karanburadkar.bookstore.repository.BookCategoryRepository;
import com.karanburadkar.bookstore.services.BookCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    private static final Logger log = LoggerFactory.getLogger(BookCategoryServiceImpl.class);
    private final BookCategoryRepository categoryRepository;

    public BookCategoryServiceImpl(BookCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<BookCategoryDto> getCategories(Integer page, Integer size, String sort, String direction) {
        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<BookCategory> categories = categoryRepository.findAll(PageRequest.of(page, size, dir, sort));
        return categories.stream()
                .map(c -> new BookCategoryDto(c.getId(), c.getName()))
                .toList();
    }

    @Override
    public BookCategoryDto getCategoryById(Long categoryId) {
        BookCategory bookCategory = resolveBookCategory(categoryId);
        return new BookCategoryDto(bookCategory.getId(), bookCategory.getName());
    }

    private BookCategory resolveBookCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with Id: {}", categoryId);
                    return new EntityNotFoundException("Book Category not found with Id: " + categoryId);
                });
    }

    @Override
    @Transactional
    public BookCategoryDto createCategory(BookCategoryDto bookCategoryDto) {
        BookCategory bookCategory = categoryRepository.findByName(bookCategoryDto.getName())
                .orElse(null);
        if (bookCategory != null) {
            String message = bookCategoryDto.getName() + ": This book category already exist";
            log.error(message);
            throw new DataIntegrityViolationException("duplicate book category insertion", message);
        }
        BookCategory savedBookCategory = categoryRepository.save(new BookCategory(bookCategoryDto.getName()));

        return new BookCategoryDto(savedBookCategory.getId(), savedBookCategory.getName());
    }

    @Override
    @Transactional
    public BookCategoryDto updateCategory(BookCategoryDto bookCategoryDto, Long categoryId) {
        BookCategory bookCategory = resolveBookCategory(categoryId);
        bookCategory.setName(bookCategory.getName());
        BookCategory saved = categoryRepository.save(bookCategory);

        return new BookCategoryDto(saved.getId(), saved.getName());
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) categoryRepository.deleteById(categoryId);
        else {
            log.error("Book Category does not exist.");
            throw new EntityNotFoundException("Book Category does not exist with Id: " + categoryId);
        }
        log.info("Book Category deleted successfully.");
    }
}
