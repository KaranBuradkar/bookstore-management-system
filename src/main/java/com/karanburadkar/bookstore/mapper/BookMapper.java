package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.BookRequestDto;
import com.karanburadkar.bookstore.dto.BookResponseDto;
import com.karanburadkar.bookstore.dto.BookShortResponseDto;
import com.karanburadkar.bookstore.entity.Author;
import com.karanburadkar.bookstore.entity.Book;
import com.karanburadkar.bookstore.entity.BookCategory;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                BookCategoryMapper.class,
                AuthorMapper.class,
                PublisherMapper.class,
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BookMapper {

    Book toEntity(BookRequestDto bookRequestDto);

    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "authors", target = "authors")
    @Mapping(source = "publisher", target = "publisher")
    BookResponseDto toDto(Book book);

    @Mapping(source = "publisher.id", target = "publisher")
    BookShortResponseDto toShortResponseDto(Book book);

    @AfterMapping
    default void extraField(Book book, @MappingTarget BookShortResponseDto bookShortResponseDto) {
        if (book == null) return;

        Set<BookCategory> categories = book.getCategories();
        if (categories != null) {
            Set<String> categoryNames = categories.stream().map(BookCategory::getName).collect(Collectors.toSet());
            bookShortResponseDto.setCategoryNames(categoryNames);
        }

        Set<Author> authors = book.getAuthors();
        if (authors != null) {
            List<Long> authorIds = authors.stream().map(Author::getId).toList();
            bookShortResponseDto.setAuthorIds(authorIds);
        }
    }

}
