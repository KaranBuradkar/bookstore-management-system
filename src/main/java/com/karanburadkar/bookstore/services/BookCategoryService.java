package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.BookCategoryDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookCategoryService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    List<BookCategoryDto> getCategories(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    BookCategoryDto getCategoryById(Long categoryId);

    @PreAuthorize("hasAnyRole('ADMIN', 'PUBLISHER', 'MANAGEMENT')")
    BookCategoryDto createCategory(BookCategoryDto bookCategoryDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    BookCategoryDto updateCategory(BookCategoryDto bookCategoryDto, Long categoryId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    void deleteCategoryById(Long categoryId);
}
