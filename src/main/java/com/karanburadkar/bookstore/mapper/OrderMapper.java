package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.OrderDto;
import com.karanburadkar.bookstore.entity.Order;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {
                AddressMapper.class,
                CustomerMapper.class,
                OrderItemMapper.class,
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderMapper {

    Order toEntity(OrderDto orderDto);

    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "address", target = "address")
    OrderDto toDto(Order order);

}
