package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.AddressDto;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.services.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/address")
public class AddressController extends BaseController{

    private final AddressService addressService;

    public AddressController(AddressService addressService, HttpServletRequest request) {
        super(request);
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAddresses(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        List<AddressDto> addresses = addressService.getAddresses(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Fetch all addresses successfully", addresses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> getAddressDtoById(@PathVariable("id") Long addressId) {
        AddressDto address = addressService.getAddressDtoById(addressId);
        return ResponseEntity.ok(buildApiResponse("Fetch address successfully", address));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(@RequestBody @Valid AddressDto addressDto) {
        AddressDto createdAddress = addressService.createAddress(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New address created successfully", createdAddress));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @RequestBody @Valid AddressDto addressDto,
            @PathVariable("id") Long addressId
    ) {
        AddressDto updatedAddress = addressService.updateAddress(addressDto, addressId);
        return ResponseEntity.ok(buildApiResponse("Address updated successfully", updatedAddress));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddressSpecific(
            @RequestBody Map<String, Object> map,
            @PathVariable("id") Long addressId
    ) {
        AddressDto updatedAddress = addressService.updateAddressSpecific(map, addressId);
        return ResponseEntity.ok(buildApiResponse("Address partially updated successfully", updatedAddress));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddressById(@PathVariable("id") Long addressId) {
        addressService.deleteAddressById(addressId);
        return ResponseEntity.ok(buildApiResponse("Address deleted successfully"));
    }
}
