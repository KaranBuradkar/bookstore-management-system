package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.CustomerRequestDto;
import com.karanburadkar.bookstore.dto.CustomerResponseDto;
import com.karanburadkar.bookstore.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    Customer toEntity(CustomerRequestDto customerRequestDto);
    CustomerResponseDto toDto(Customer customer);
}
