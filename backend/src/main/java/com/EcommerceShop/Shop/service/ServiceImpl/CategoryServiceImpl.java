package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.repository.CategoryRepository;
import com.EcommerceShop.Shop.dto.request.CategoryRequest;
import com.EcommerceShop.Shop.dto.response.CategoryResponse;
import com.EcommerceShop.Shop.entity.Category;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.mapper.CategoryMapper;
import com.EcommerceShop.Shop.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.util.List;

import static org.apache.tomcat.util.IntrospectionUtils.capitalize;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository ;
    CategoryMapper categoryMapper ;

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse create(CategoryRequest request){
        request.setName(formatText(request.getName()));
        if(categoryRepository.findByName(request.getName()) != null){
            throw new AppException(ErrorCode.CATEGORY_EXISTED) ;
        }

        Category category = categoryMapper.toCategory(request)  ;
        categoryRepository.save(category) ;
        return categoryMapper.toCategoryResponse(category) ;
    }

    public List<CategoryResponse> getALl(){
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList() ;
    }

    @CacheEvict(value = "products", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)) ;
        categoryMapper.update(category,request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category)) ;
    }
    @CacheEvict(value = "products", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)) ;
        categoryRepository.delete(category);
    }
    public  Category getByName(String name){
        Category category = categoryRepository.findByName(formatText(name)) ;
        if(category == null) throw new AppException(ErrorCode.CATEGORY_NOT_FOUND) ;
        return category ;
    }

    private String formatText(String text){
        if(text == null || text.isBlank()) return text ;
        text = text.toLowerCase() ;
        String[] words = text.trim().split(" ") ;
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(capitalize(word)).append(" ");
            }
        }
        return result.toString().trim();
    }
}
