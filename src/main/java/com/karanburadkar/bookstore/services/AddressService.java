package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.AddressDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AddressService {

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    List<AddressDto> getAddresses(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'CUSTOMER')")
    AddressDto getAddressDtoById(Long addressId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'CUSTOMER')")
    AddressDto createAddress(AddressDto addressDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'CUSTOMER')")
    AddressDto updateAddress(AddressDto addressDto, Long addressId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT', 'CUSTOMER')")
    AddressDto updateAddressSpecific(Map<String, Object> map, Long addressId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT')")
    void deleteAddressById(Long addressId);
}
