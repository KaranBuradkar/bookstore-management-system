package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.BookRequestDto;
import com.karanburadkar.bookstore.dto.BookResponseDto;
import com.karanburadkar.bookstore.dto.BookShortResponseDto;
import com.karanburadkar.bookstore.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/books")
public class BookController extends BaseController {

    private final BookService bookService;

    public BookController(HttpServletRequest request, BookService bookService) {
        super(request);
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getBooks(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "title") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        List<BookResponseDto> books = bookService.getBooks(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Books fetched successfully", books));
    }

    @GetMapping("/short")
    public ResponseEntity<ApiResponse<List<BookShortResponseDto>>> getBooksShort(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "title") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        List<BookShortResponseDto> books = bookService.getBooksShort(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Books fetched successfully", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@PathVariable("id") Long bookId) {
        BookResponseDto bookResponseDto = bookService.getBookById(bookId);
        return ResponseEntity.ok(buildApiResponse("Book details fetched for ID: " + bookId, bookResponseDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(
            @RequestBody @Valid BookRequestDto bookRequestDto) {

        BookResponseDto createdBook = bookService.createBook(bookRequestDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBook.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(buildApiResponse("New book created successfully", createdBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> updateBook(
            @RequestBody @Valid BookRequestDto bookRequestDto, @PathVariable("id") Long bookId) {
        BookResponseDto updatedBookResponseDto = bookService.updateBook(bookRequestDto, bookId);
        return ResponseEntity.ok(buildApiResponse(
                "Book updated successfully for ID: " + bookId, updatedBookResponseDto));
    }

    @PutMapping("/{bookId}/authors/{authorId}")
    public ResponseEntity<ApiResponse<Void>> addAuthorById(
            @PathVariable("bookId") Long bookId,
            @PathVariable("authorId") Long authorId
    ) {
        bookService.addAuthorById(bookId, authorId);
        return ResponseEntity.ok(buildApiResponse("Author added in book successfully"));
    }

    @PutMapping("/{bookId}/authors/{authorId}/replace-by/{newAuthorId}")
    public ResponseEntity<ApiResponse<Void>> addAuthorById(
            @PathVariable("bookId") Long bookId,
            @PathVariable("authorId") Long authorId,
            @PathVariable("newAuthorId") Long newAuthorId
    ) {
        bookService.replaceAuthorById(bookId, authorId, newAuthorId);
        return ResponseEntity.ok(buildApiResponse("Author replacement successful"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> updateBookSpecifics(
            @RequestBody Map<String, Object> map, @PathVariable("id") Long bookId) {
        BookResponseDto updatedBookResponseDto = bookService.updateBookSpecifics(map, bookId);
        return ResponseEntity.ok(buildApiResponse(
                "Book partially updated successfully for ID: " + bookId, updatedBookResponseDto));
    }

    @DeleteMapping("/{bookId}/authors/{authorId}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthorById(
            @PathVariable("bookId") Long bookId,
            @PathVariable("authorId") Long authorId
    ) {
        bookService.deleteAuthorById(bookId, authorId);
        return ResponseEntity.ok(buildApiResponse("Author removed from the book successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBookById(@PathVariable("id") Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.ok(buildApiResponse("Book deleted successfully"));
    }
}
