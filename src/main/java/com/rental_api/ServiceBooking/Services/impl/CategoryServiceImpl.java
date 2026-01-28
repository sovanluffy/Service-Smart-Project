package com.rental_api.ServiceBooking.Services.impl;

import com.rental_api.ServiceBooking.Dto.Request.CategoryRequest;
import com.rental_api.ServiceBooking.Dto.Response.CategoryResponse;
import com.rental_api.ServiceBooking.Entity.Category;
import com.rental_api.ServiceBooking.Repository.CategoryRepository;
import com.rental_api.ServiceBooking.Services.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        // Convert DTO to Entity
        Category category = new Category();
        category.setName(request.getName());

        Category savedCategory = repository.save(category);

        // Convert Entity to Response DTO
        return mapToResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CategoryResponse mapToResponse(Category category) {
    return new CategoryResponse(
        category.getId(),
        category.getName(),
        category.getDescription() // if you added description
    );
}
}
