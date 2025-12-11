package com.karanburadkar.bookstore.mapper;

import com.karanburadkar.bookstore.dto.AddressDto;
import com.karanburadkar.bookstore.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
}
