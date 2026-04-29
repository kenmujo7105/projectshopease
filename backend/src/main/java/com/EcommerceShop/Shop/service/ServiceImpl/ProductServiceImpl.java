package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.entity.Brand;
import com.EcommerceShop.Shop.repository.BrandRepository;
import com.EcommerceShop.Shop.entity.Category;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.entity.Product;
import com.EcommerceShop.Shop.entity.ProductCategory;
import com.EcommerceShop.Shop.entity.ProductDetail;
import com.EcommerceShop.Shop.dto.request.ProductDetailRequest;
import com.EcommerceShop.Shop.dto.request.ProductRequest;
import com.EcommerceShop.Shop.dto.request.UpdateProductDetailRequest;
import com.EcommerceShop.Shop.dto.response.ProductResponse;
import com.EcommerceShop.Shop.mapper.ProductMapper;
import com.EcommerceShop.Shop.repository.ProductRepository;
import com.EcommerceShop.Shop.service.CategoryService;
import com.EcommerceShop.Shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    CategoryService categoryService;
    BrandRepository brandRepository ;

    ProductMapper productMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        List<ProductDetail> productDetails = request.getProductDetails().stream()
                .map(detail -> ProductDetail.builder()
                        .price(detail.getPrice())
                        .quantity(detail.getQuantity())
                        .info(detail.getInfo())
                        .product(product).build()).toList();
        product.getProductDetails().addAll(productDetails);

        List<Category> categories = request.getProductCategory().stream()
                .map(categoryService::getByName).toList();

        List<ProductCategory> productCategories = categories.stream()
                .map(category -> ProductCategory.builder()
                        .product(product)
                        .category(category).build()).toList();
        product.getProductCategories().addAll(productCategories);
        if(request.getBrand() != null){
            Optional<Brand> brand = brandRepository.findByName(request.getBrand()) ;
            if(brand.isPresent()){
                product.setBrand(brand.get());
                brand.get().getProductList().add(product) ;
//            brandRepository.save(brand) ;
            }
        }
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse addADetailToProduct(Long productId, ProductDetailRequest request) {
        Product product  = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)) ;
        ProductDetail productDetail = ProductDetail.builder()
                .info(request.getInfo())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .product(product).build();
        product.getProductDetails().add(productDetail) ;
        return productMapper.toProductResponse(productRepository.save(product)) ;
    }

    public List<ProductResponse> getByCategory(String name) {
        Category category = categoryService.getByName(name) ;
        return category.getProductCategories().stream()
                .map(productCategory -> productMapper.toProductResponse(productCategory.getProduct()))
                .toList();
    }

    public List<ProductResponse> getByBrand(Long brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProductInfo(Long id, ProductRequest request){
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)) ;
        productMapper.update(product,request);
        return productMapper.toProductResponse(product) ;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProductDetail(Long productId, UpdateProductDetailRequest request){
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)) ;
        ProductDetail productDetail = product.getProductDetails().stream().filter(detail -> detail.getId().equals(request.getId())).findFirst().orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)) ;

        if (request.getInfo() != null) productDetail.setInfo(request.getInfo());
        if (request.getPrice() != null) productDetail.setPrice(request.getPrice());
        if (request.getQuantity() != null) productDetail.setQuantity(request.getQuantity());

        return productMapper.toProductResponse(product) ;
    }

    public List<ProductResponse> getProductPaging(Pageable pageable) {
        return productRepository.findAll(pageable.isPaged() ? pageable : Pageable.unpaged() ).stream().map(productMapper::toProductResponse).toList() ;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))  ;
        productRepository.delete(product);
    }

    public ProductResponse getProductById(Long productId){
        return productMapper.toProductResponse(productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))) ;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDetail(Long productId, Long detailId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)) ;
        ProductDetail item = product.getProductDetails().stream().filter(detail -> detail.getId().equals(detailId)).findFirst().orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)) ;
        product.getProductDetails().remove(item) ;
        productRepository.save(product) ;
    }

    public List<ProductResponse> search(String keyword){
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
        }
        return productRepository.search(keyword.toLowerCase()).stream().map(productMapper::toProductResponse).toList();
    }

    public List<ProductResponse> suggest(String keyword){
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return productRepository.suggest(keyword.toLowerCase(), PageRequest.of(0, 7))
                .stream().map(productMapper::toProductResponse).toList();
    }
}
