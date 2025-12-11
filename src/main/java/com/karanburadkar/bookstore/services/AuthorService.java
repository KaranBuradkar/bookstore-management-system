package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.AuthorDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AuthorService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    List<AuthorDto> getAuthors(Integer page, Integer size, String sort, String direction, Long bookId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    AuthorDto getAuthorById(Long id);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER')")
    AuthorDto createAuthor(AuthorDto authorDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR (hasAnyRole('PUBLISHER') " +
            "AND @permissionGuard.isOwnerByAuthorId(#authorId, authentication.principal.id))")
    AuthorDto updateAuthor(AuthorDto authorDto, Long authorId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER')")
    AuthorDto updateAuthorSpecific(Map<String, Object> map, Long authorId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER')")
    void deleteAuthorById(Long authorId);

}
