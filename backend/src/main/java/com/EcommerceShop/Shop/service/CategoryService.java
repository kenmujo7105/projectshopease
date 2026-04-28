package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.CategoryRequest;
import com.EcommerceShop.Shop.dto.response.CategoryResponse;
import com.EcommerceShop.Shop.entity.Category;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


public interface CategoryService {
    CategoryResponse create(CategoryRequest request);

    List<CategoryResponse> getALl();

    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);

    void deleteCategory(Long categoryId);

    Category getByName(String name);
}
