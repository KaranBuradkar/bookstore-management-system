package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.AddressDto;
import com.karanburadkar.bookstore.entity.Address;
import com.karanburadkar.bookstore.exception.business.DuplicateResourceException;
import com.karanburadkar.bookstore.exception.database.AddressNotFoundException;
import com.karanburadkar.bookstore.mapper.AddressMapper;
import com.karanburadkar.bookstore.repository.AddressRepository;
import com.karanburadkar.bookstore.services.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public List<AddressDto> getAddresses(Integer page, Integer size, String sort, String direction) {
        Sort.Direction dir = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Address> addressPage = addressRepository.findAll(PageRequest.of(page, size, dir, sort));

        return addressPage.stream().map(addressMapper::toDto).toList();
    }

    @Override
    public AddressDto getAddressDtoById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address not found");
                    return new AddressNotFoundException(addressId);
                });
        return addressMapper.toDto(address);
    }

    @Override
    @Transactional
    public AddressDto createAddress(AddressDto addressDto) {

        validateAddress(addressDto);

        Address entity = addressMapper.toEntity(addressDto);
        Address saved = addressRepository.save(entity);
        log.info("New address entry created.");

        return addressMapper.toDto(saved);
    }

    private void validateAddress(AddressDto a) {
        if (a.getId() != null) {
            String message = "while creating address do not required addressId";
            log.error(message);
            throw new IllegalArgumentException(message+": "+a.getId());
        }

        Address address = addressRepository.findByAddressDetail(
                a.getLandmark(),
                a.getCity(),
                a.getDistrict(),
                a.getState(),
                a.getCountry(),
                a.getPostalCode()
        ).orElse(null);

        if (address != null) {
            String message = "Attempt to duplicate address creation Instead use below pre-existing " +
                    "address with addressId: "+address.getId();
            log.warn(message);
            throw new DuplicateResourceException(message, addressMapper.toDto(address));
        }
    }

    @Override
    @Transactional
    public AddressDto updateAddress(AddressDto addressDto, Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            String message = "Address does not exist";
            log.error(message);
            throw new IllegalArgumentException(message+" addressId: "+addressId);
        }
        Address entity = addressMapper.toEntity(addressDto);
        entity.setId(addressId);

        Address updated = addressRepository.save(entity);
        return addressMapper.toDto(updated);
    }

    @Override
    @Transactional
    public AddressDto updateAddressSpecific(Map<String, Object> map, Long addressId) {
        Address address = addressById(addressId);
        map.forEach((key, value) -> {
            switch (key) {
                case "landmark" -> address.setLandmark((String) value);
                case "city" -> address.setCity((String) value);
                case "district" -> address.setDistrict((String) value);
                case "state" -> address.setState((String) value);
                case "country" -> address.setCountry((String) value);
                case "postalCode" -> address.setPostalCode((String) value);
                default -> {
                    String message = "Invalid key for update Address";
                    log.error(message);
                    throw new IllegalArgumentException(message+" with addressId: "+addressId);
                }
            }
        });
        Address updated = addressRepository.save(address);
        return addressMapper.toDto(updated);
    }

    private Address addressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.warn("Address not found with addressId: {}", addressId);
                    return new AddressNotFoundException(addressId);
                });
    }

    @Override
    @Transactional
    public void deleteAddressById(Long addressId) {
        Address address = addressById(addressId);
        addressRepository.delete(address);
        log.info("Address entry deleted successfully.");
    }
}
