package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.PublisherDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PublisherService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    List<PublisherDto> getPublishers(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'PUBLISHER', 'CUSTOMER')")
    PublisherDto getPublisherById(Long publisherId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    PublisherDto createPublisher(PublisherDto publisherDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR " +
            "AND @permissionGuard.isOwnerByPublisherId(#publisherId, authentication.principal.id)")
    PublisherDto updatePublisher(PublisherDto publisherDto, Long publisherId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR " +
            "AND @permissionGuard.isOwnerByPublisherId(#publisherId, authentication.principal.id)")
    PublisherDto updatePublisherSpecific(Map<String, Object> map, Long publisherId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR " +
            "AND @permissionGuard.isOwnerByPublisherId(#publisherId, authentication.principal.id)")
    void deletePublisherById(Long publisherId);
}
