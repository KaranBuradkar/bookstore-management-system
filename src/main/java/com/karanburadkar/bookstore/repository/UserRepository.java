package com.karanburadkar.bookstore.repository;

import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.entity.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u From User u left join fetch u.roles WHERE u.username = ?1")
    Optional<User> findUserWithRolesByUsername(String username);

    Set<RoleType> findRolesByUsername(String username);
}
