package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.BookCategoryDto;
import com.karanburadkar.bookstore.services.BookCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bookCategory")
public class BookCategoryController extends BaseController {

    private final BookCategoryService categoryService;

    public BookCategoryController(BookCategoryService categoryService, HttpServletRequest request) {
        super(request);
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookCategoryDto>>> getCategories(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {
        List<BookCategoryDto> categories = categoryService.getCategories(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Categories fetched successfully", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookCategoryDto>> getCategoryById(@PathVariable("id") Long categoryId) {
        BookCategoryDto category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(buildApiResponse("Category fetched successfully", category));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookCategoryDto>> createCategory(
            @RequestBody @Valid BookCategoryDto bookCategoryDto) {
        BookCategoryDto created = categoryService.createCategory(bookCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New category created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookCategoryDto>> updateCategory(
            @RequestBody @Valid BookCategoryDto bookCategoryDto,
            @PathVariable("id") Long categoryId) {
        BookCategoryDto updated = categoryService.updateCategory(bookCategoryDto, categoryId);
        return ResponseEntity.ok(buildApiResponse("Category updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoryById(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok(buildApiResponse("Category deleted successfully"));
    }
}
