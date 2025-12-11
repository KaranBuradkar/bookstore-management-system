package com.karanburadkar.bookstore.services;

import com.karanburadkar.bookstore.dto.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    @PreAuthorize("hasAnyRole('ADMIN')")
    List<UserDto> getUsers(Integer page, Integer size, String sort, String direction);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR #userId == authentication.principal.id")
    UserDto getUserById(Long userId);

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGEMENT') OR #userId == authentication.principal.id")
    void deleteUserById(Long userId);
}
