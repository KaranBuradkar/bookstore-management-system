package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.landmark = ?1 AND a.city = ?2 " +
            "AND a.district = ?3 AND a.state = ?4 AND a.country = ?5 AND a.postalCode = ?6")
    Optional<Address> findByAddressDetail(String landmark, String city, String district,
                                          String state, String country, String postalCode);
}
