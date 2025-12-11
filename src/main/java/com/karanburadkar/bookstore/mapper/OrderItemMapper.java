package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.OrderItemDto;
import com.karanburadkar.bookstore.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemDto orderItems);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItems);
}
