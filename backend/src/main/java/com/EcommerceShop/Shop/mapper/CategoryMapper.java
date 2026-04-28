package com.EcommerceShop.Shop.mapper;

import com.EcommerceShop.Shop.dto.request.CategoryRequest;
import com.EcommerceShop.Shop.dto.response.CategoryResponse;
import com.EcommerceShop.Shop.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    Category toCategory(CategoryRequest request) ;
    CategoryResponse toCategoryResponse(Category category) ;

    void update(@MappingTarget Category category, CategoryRequest request) ;
}
