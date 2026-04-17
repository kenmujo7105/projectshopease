package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.service.CategoryService;
import com.EcommerceShop.Shop.dto.request.CategoryRequest;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.dto.response.CategoryResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService ;

    @PostMapping
    ApiResponseWrapper<CategoryResponse> create(@RequestBody CategoryRequest request){
        return ApiResponseWrapper.<CategoryResponse>builder()
                .data(categoryService.create(request)).build();
    }

    @GetMapping("/all")
    ApiResponseWrapper<List<CategoryResponse>> getAll(){
        return ApiResponseWrapper.<List<CategoryResponse>>builder()
                .data(categoryService.getALl()).build() ;
    }

    @PutMapping("/{categoryId}")
    ApiResponseWrapper<CategoryResponse> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest request){
        return ApiResponseWrapper.<CategoryResponse>builder()
                .data(categoryService.updateCategory(categoryId,request)).build();
    }

    @DeleteMapping("/{categoryId}")
    ApiResponseWrapper<?> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ApiResponseWrapper.builder()
                .code(200)
                .message("Category has been deleted!").build();
    }

}
