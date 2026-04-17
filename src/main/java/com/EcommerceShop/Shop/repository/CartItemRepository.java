package com.EcommerceShop.Shop.repository;

import com.EcommerceShop.Shop.entity.Cart;
import com.EcommerceShop.Shop.entity.CartItem;
import com.EcommerceShop.Shop.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCart(Cart cart) ;
    CartItem findByItem(ProductDetail item) ;
}
