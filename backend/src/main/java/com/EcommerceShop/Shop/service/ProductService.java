package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.ProductDetailRequest;
import com.EcommerceShop.Shop.dto.request.ProductRequest;
import com.EcommerceShop.Shop.dto.request.UpdateProductDetailRequest;
import com.EcommerceShop.Shop.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);

    ProductResponse addADetailToProduct(Long productId, ProductDetailRequest request);

    List<ProductResponse> getByCategory(String name) ;

    List<ProductResponse> getByBrand(Long brandId) ;

    ProductResponse updateProductInfo(Long id, ProductRequest request);

    ProductResponse updateProductDetail(Long productId, UpdateProductDetailRequest request);

    List<ProductResponse> getProductPaging(Pageable pageable) ;

    void deleteProduct(Long productId);

    ProductResponse getProductById(Long productId);

    void deleteDetail(Long productId, Long detailId) ;

    List<ProductResponse> search(String keyword) ;

    List<ProductResponse> suggest(String keyword) ;
}
