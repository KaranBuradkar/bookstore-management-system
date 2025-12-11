package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.BookCategoryDto;
import com.karanburadkar.bookstore.entity.BookCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookCategoryMapper {
    BookCategory toEntity(BookCategoryDto bookCategoryDto);
    BookCategoryDto toDto(BookCategory bookCategory);
}
