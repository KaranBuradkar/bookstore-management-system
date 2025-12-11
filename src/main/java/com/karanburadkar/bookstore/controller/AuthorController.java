package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.AuthorDto;
import com.karanburadkar.bookstore.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/authors")
public class AuthorController extends BaseController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService, HttpServletRequest request) {
        super(request);
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorDto>>> getAuthors(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "bookId", required = false) Long bookId) {

        List<AuthorDto> authors = authorService.getAuthors(page, size, sort, direction, bookId);
        return ResponseEntity.ok(buildApiResponse("Authors fetched successfully", authors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDto>> getAuthorById(@PathVariable("id") Long authorId) {
        AuthorDto author = authorService.getAuthorById(authorId);
        return ResponseEntity.ok(buildApiResponse("Author fetched successfully", author));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorDto>> createAuthor(@RequestBody @Valid AuthorDto authorDto) {
        AuthorDto created = authorService.createAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New author created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDto>> updateAuthor(
            @RequestBody @Valid AuthorDto authorDto,
            @PathVariable("id") Long authorId) {
        AuthorDto updated = authorService.updateAuthor(authorDto, authorId);
        return ResponseEntity.ok(buildApiResponse("Author updated successfully", updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDto>> updateAuthorSpecific(
            @RequestBody Map<String, Object> map,
            @PathVariable("id") Long authorId) {
        AuthorDto updated = authorService.updateAuthorSpecific(map, authorId);
        return ResponseEntity.ok(buildApiResponse("Author partially updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthorById(@PathVariable("id") Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok(buildApiResponse("Author deleted successfully"));
    }
}
