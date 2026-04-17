package com.EcommerceShop.Shop.mapper;

import com.EcommerceShop.Shop.entity.Brand;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.entity.Product;
import com.EcommerceShop.Shop.entity.ProductDetail;
import com.EcommerceShop.Shop.dto.request.ProductRequest;
import com.EcommerceShop.Shop.dto.response.ProductDetailResponse;
import com.EcommerceShop.Shop.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .productDetails(new ArrayList<>())
                .productCategories(new ArrayList<>())
                .averageRate(0.0)
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .feedback(new ArrayList<>())
                .build();
    }

    public ProductResponse toProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .averageRate(product.getAverageRate())
                .productDetail(product.getProductDetails().stream().map(this::toProductDetailResponse).toList())
                .brand(toBrand(product.getBrand()))
                .category(product.getProductCategories().stream().map(x -> x.getCategory().getName()).toList())
                .minPrice(getMinPrice(product.getProductDetails()))
                .numRate((long) product.getFeedback().size())
                .build();
    }
    String toBrand(Brand brand){
        if(brand == null) return null ;
        return brand.getName() ;
    }
    public abstract ProductDetailResponse toProductDetailResponse(ProductDetail productDetail) ;

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productDetails", ignore = true )
    public abstract void update(@MappingTarget Product product, ProductRequest productRequest) ;

    Double getMinPrice(List<ProductDetail> productDetails){
        return productDetails.stream().mapToDouble(ProductDetail::getPrice).min().orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST));
    }
}
