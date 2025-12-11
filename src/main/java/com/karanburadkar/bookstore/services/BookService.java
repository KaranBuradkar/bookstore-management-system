package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.BookRequestDto;
import com.karanburadkar.bookstore.dto.BookResponseDto;
import com.karanburadkar.bookstore.dto.BookShortResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BookService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    List<BookResponseDto> getBooks(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    List<BookShortResponseDto> getBooksShort(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    BookResponseDto getBookById(Long bookId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByPublisherId(#bookRequestDto.publisherId, authentication.principal.id)")
    BookResponseDto createBook(BookRequestDto bookRequestDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByBookId(#bookId, authentication.principal.id)")
    BookResponseDto updateBook(BookRequestDto bookRequestDto, Long bookId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByBookId(#bookId, authentication.principal.id)")
    BookResponseDto updateBookSpecifics(Map<String, Object> map, Long bookId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByBookId(#bookId, authentication.principal.id)")
    void deleteBookById(Long bookId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByBookId(#bookId, authentication.principal.id)")
    void deleteAuthorById(Long bookId, Long authorId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByBookId(#bookId, authentication.principal.id)")
    void addAuthorById(Long bookId, Long authorId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR hasRole('PUBLISHER') AND " +
            "@permissionGuard.isOwnerByBookId(#bookId, authentication.principal.id)")
    void replaceAuthorById(Long bookId, Long oldAuthorId, Long newAuthorId);
}
