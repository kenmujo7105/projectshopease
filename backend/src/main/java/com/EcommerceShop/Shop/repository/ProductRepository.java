package com.EcommerceShop.Shop.repository;

import com.EcommerceShop.Shop.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
       SELECT p
       FROM Product p
       JOIN p.productCategories pc
       JOIN pc.category c
       JOIN p.brand b
       WHERE lower(p.name) LIKE concat('%', :keyword, '%')
          OR lower(c.name) LIKE concat('%', :keyword, '%')
          OR lower(b.name) LIKE concat('%', :keyword, '%')
       """)
    List<Product> search(@Param("keyword") String keyword) ;
}
