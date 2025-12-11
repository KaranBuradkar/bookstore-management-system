package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.UserDto;
import com.karanburadkar.bookstore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(HttpServletRequest request, UserService userService) {
        super(request);
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {

        List<UserDto> users = userService.getUsers(page, size, sort, direction);
        return ResponseEntity.ok(buildApiResponse("Users fetched successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable("id") Long userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(buildApiResponse("User fetched successfully for ID: " + userId, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable("id") Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok(buildApiResponse("User deleted successfully"));
    }
}
