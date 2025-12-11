package com.karanburadkar.bookstore.controller;

import com.karanburadkar.bookstore.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/public")
public class HomeController extends BaseController{

    protected HomeController(HttpServletRequest request) {
        super(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> welcome() {
        return ResponseEntity.ok(buildApiResponse("Welcome to Book Store Management System"));
    }
}
