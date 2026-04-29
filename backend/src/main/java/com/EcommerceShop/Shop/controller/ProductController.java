package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.service.ProductService;
import com.EcommerceShop.Shop.dto.request.ProductDetailRequest;
import com.EcommerceShop.Shop.dto.request.ProductRequest;
import com.EcommerceShop.Shop.dto.request.UpdateProductDetailRequest;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.dto.response.ProductResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class ProductController {
    ProductService productService ;

    @GetMapping
    ApiResponseWrapper<List<ProductResponse>> getProductsPaging(Pageable pageable){
        return ApiResponseWrapper.<List<ProductResponse>>builder()
                .data(productService.getProductPaging(pageable)).build();
    }

    @GetMapping("/{productId}")
    ApiResponseWrapper<ProductResponse> getProductById(@PathVariable Long productId){
        return ApiResponseWrapper.<ProductResponse>builder()
                .data(productService.getProductById(productId)).build();
    }

    @PostMapping
    ApiResponseWrapper<ProductResponse> create(@RequestBody ProductRequest request){
        return ApiResponseWrapper.<ProductResponse>builder()
                .data(productService.create(request)).build();
    }

    @PostMapping("/{productId}/detail")
    ApiResponseWrapper<ProductResponse> addADetailToProduct(@PathVariable Long productId, @RequestBody ProductDetailRequest request){
        return ApiResponseWrapper.<ProductResponse>builder()
                .data(productService.addADetailToProduct(productId,request)).build();
    }

    @PutMapping("/{productId}/info")
    ApiResponseWrapper<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest request){
        return ApiResponseWrapper.<ProductResponse>builder()
                .data(productService.updateProductInfo(productId,request)).build() ;
    }

    @PutMapping("/{productId}/detail")
    ApiResponseWrapper<ProductResponse> updateProductDetail(@PathVariable Long productId, @RequestBody UpdateProductDetailRequest request){
        return ApiResponseWrapper.<ProductResponse>builder()
                .data(productService.updateProductDetail(productId,request)).build() ;
    }

    @DeleteMapping("/{productId}")
    ApiResponseWrapper<?> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ApiResponseWrapper.builder()
                .code(200)
                .message(String.format("Product %s have been deleted", productId))
                .build();
    }

    @DeleteMapping("/{productId}/detail/{detailId}")
    ApiResponseWrapper<?> deleteDetail(@PathVariable Long productId, @PathVariable Long detailId){
        productService.deleteDetail(productId, detailId);
        return ApiResponseWrapper.builder()
                .code(200)
                .message(String.format("Product detail %s has been deleted!", detailId)).build();
    }

    @GetMapping("/by-category")
    ApiResponseWrapper<List<ProductResponse>> getByCategory(
            @RequestParam(name = "name") String name
    ){
        return ApiResponseWrapper.<List<ProductResponse>>builder()
                .data(productService.getByCategory(name)).build();
    }

    @GetMapping("/by-brand")
    ApiResponseWrapper<List<ProductResponse>> getByBrand(
            @RequestParam(name = "brandId") Long brandId
    ){
        return ApiResponseWrapper.<List<ProductResponse>>builder()
                .data(productService.getByBrand(brandId)).build();
    }

    @GetMapping("/search")
    ApiResponseWrapper<List<ProductResponse>> search(
            @RequestParam(name = "keyword") String keyword
    ){
        return ApiResponseWrapper.<List<ProductResponse>>builder()
                .data(productService.search(keyword)).build() ;
    }
    @GetMapping("/suggest")
    ApiResponseWrapper<List<ProductResponse>> suggest(
            @RequestParam(name = "keyword") String keyword
    ){
        return ApiResponseWrapper.<List<ProductResponse>>builder()
                .data(productService.suggest(keyword)).build();
    }

}
