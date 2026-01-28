package com.rental_api.ServiceBooking.Controller;


import com.rental_api.ServiceBooking.Dto.Request.CategoryRequest;
import com.rental_api.ServiceBooking.Dto.Response.CategoryResponse;
import com.rental_api.ServiceBooking.Services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;




@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Service Category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}