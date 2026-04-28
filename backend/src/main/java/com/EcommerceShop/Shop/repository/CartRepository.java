package com.EcommerceShop.Shop.repository;

import com.EcommerceShop.Shop.entity.Cart;
import com.EcommerceShop.Shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {
    Cart findByUser(User user) ;
}
