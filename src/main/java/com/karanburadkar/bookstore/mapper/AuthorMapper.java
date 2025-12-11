package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.AuthorDto;
import com.karanburadkar.bookstore.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {
    Author toEntity(AuthorDto authorDto);
    AuthorDto toDto(Author author);
}
