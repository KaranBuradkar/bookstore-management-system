package com.karanburadkar.bookstore.dto;

import java.time.Instant;

public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private String path;
    private final Instant timestamp;

    // Constructor for responses with no data
    public ApiResponse(boolean success, String message, String path) {
        this(success, message, null, path);
    }

    // Constructor for responses with data
    public ApiResponse(boolean success, String message, T data, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now(); // automatically set timestamp
        this.path = path;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
