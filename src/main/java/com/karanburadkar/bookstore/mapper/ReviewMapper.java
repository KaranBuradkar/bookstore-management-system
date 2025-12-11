package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.ReviewAddRequestDto;
import com.karanburadkar.bookstore.dto.ReviewResponseDto;
import com.karanburadkar.bookstore.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    Review toEntity(ReviewAddRequestDto reviewAddRequestDto);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "book.id", target = "bookId")
    ReviewResponseDto toResponseDto(Review review);
}
