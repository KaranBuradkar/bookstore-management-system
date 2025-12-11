package com.karanburadkar.bookstore.services.impl;

import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.dto.CustomerRequestDto;
import com.karanburadkar.bookstore.dto.CustomerResponseDto;
import com.karanburadkar.bookstore.entity.Customer;
import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.exception.database.CustomerNotFoundException;
import com.karanburadkar.bookstore.exception.database.UserNotFoundException;
import com.karanburadkar.bookstore.mapper.CustomerMapper;
import com.karanburadkar.bookstore.repository.CustomerRepository;
import com.karanburadkar.bookstore.repository.UserRepository;
import com.karanburadkar.bookstore.services.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository,
                               CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.customerMapper = customerMapper;
    }


    @Override
    public CustomerResponseDto getCustomerById(Long customerId) {
        Customer customer = resolveCustomer(customerId);
        return customerMapper.toDto(customer);
    }

    private Customer resolveCustomer(Long customerId) {
        return customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> {
                    log.warn("Customer does not exit with customerId: {}", customerId);
                    return new CustomerNotFoundException(customerId);
                });
    }

    @Override
    public List<CustomerResponseDto> getCustomers(Integer page, Integer size, String sort, String direction) {
        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size, Sort.by(dir, sort)));

        return customerPage.stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomerById(CustomerRequestDto customerRequestDto, Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            String message = "Customer not found";
            log.warn(message);
            throw new CustomerNotFoundException(customerId);
        }

        Customer entity = customerMapper.toEntity(customerRequestDto);
        entity.setId(customerId);

        Customer updated = customerRepository.save(entity);

        return customerMapper.toDto(updated);
    }

    private User resolveUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message  = "provided invalid userId: "+userId;
                    log.warn(message);
                    return new UserNotFoundException(userId);
                });
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomerSpecifics(Map<String, Object> map, Long customerId) {
        Customer customer = resolveCustomer(customerId);

        map.forEach((key, value) -> {
            switch (key) {
                case "phoneNo" -> customer.setPhoneNo((String) value);
                case "userId", "user" -> {
                    User entity = resolveUser((Long) value);
                    customer.setUser(entity);
                }
                default -> {
                    String message = "Invalid key provided -> "+key;
                    log.warn(message);
                    throw new IllegalArgumentException(message);
                }
            }
        });

        Customer updated = customerRepository.save(customer);
        return customerMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteCustomerById(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            String message = "Attempt to delete non-exiting customer";
            log.error(message);
            throw new CustomerNotFoundException(message+" with customerId: "+customerId);
        }
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully.");
    }
}
