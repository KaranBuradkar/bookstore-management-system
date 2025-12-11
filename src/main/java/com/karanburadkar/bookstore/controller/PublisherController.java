package com.karanburadkar.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.karanburadkar.bookstore.dto.ApiResponse;
import com.karanburadkar.bookstore.dto.PublisherDto;
import com.karanburadkar.bookstore.services.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/publishers")
public class PublisherController extends BaseController {

    private final PublisherService publisherService;

    public PublisherController(HttpServletRequest request, PublisherService publisherService) {
        super(request);
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PublisherDto>>> getPublishers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        List<PublisherDto> publishers = publisherService.getPublishers(page, size, sort, direction);
        String message = "Publishers fetched successfully";
        if (publishers.isEmpty()) message = "there are none publishers";
        return ResponseEntity.ok(buildApiResponse(message, publishers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherDto>> getPublisherById(@PathVariable("id") Long publisherId) {
        PublisherDto publisher = publisherService.getPublisherById(publisherId);
        return ResponseEntity.ok(buildApiResponse("Publisher fetched successfully for ID: " + publisherId, publisher));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PublisherDto>> createPublisher(@RequestBody @Valid PublisherDto publisherDto) {
        PublisherDto created = publisherService.createPublisher(publisherDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildApiResponse("New publisher created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherDto>> updatePublisher(
            @RequestBody @Valid PublisherDto publisherDto, @PathVariable("id") Long publisherId) {

        PublisherDto updated = publisherService.updatePublisher(publisherDto, publisherId);
        return ResponseEntity.ok(buildApiResponse("Publisher updated successfully for ID: " + publisherId, updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherDto>> updatePublisherSpecific(
            @RequestBody Map<String, Object> map, @PathVariable("id") Long publisherId) {

        PublisherDto updated = publisherService.updatePublisherSpecific(map, publisherId);
        return ResponseEntity.ok(buildApiResponse("Publisher partially updated for ID: " + publisherId, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePublisherById(@PathVariable("id") Long publisherId) {
        publisherService.deletePublisherById(publisherId);
        return ResponseEntity.ok(buildApiResponse("Publisher deleted successfully"));
    }
}
