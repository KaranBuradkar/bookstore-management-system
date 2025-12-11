package com.karanburadkar.bookstore.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testValidateUser() {
        Long l = customerRepository.validateUser(2l);
        System.out.println(l);
    }

}
