package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.karanburadkar.bookstore.dto.ApiResponse;

public abstract class BaseController {

    private final HttpServletRequest request;

    protected BaseController(HttpServletRequest request) {
        this.request = request;
    }

    protected <T> ApiResponse<T> buildApiResponse(String message, T data) {
        return new ApiResponse<>(true, message, data, request.getRequestURI());
    }

    protected <T> ApiResponse<T> buildApiResponse(String message) {
        return new ApiResponse<>(true, message, request.getRequestURI());
    }
}
