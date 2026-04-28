package com.EcommerceShop.Shop.mapper;

import com.EcommerceShop.Shop.dto.request.CartItemCreateRequest;
import com.EcommerceShop.Shop.dto.response.CartResponse;
import com.EcommerceShop.Shop.entity.Cart;
import com.EcommerceShop.Shop.entity.CartItem;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CartMapper {
    @Autowired
    private CartItemMapper cartItemMapper ;
    public abstract Cart toCart(CartItemCreateRequest request) ;
    public CartResponse toCartResponse(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .user_id(cart.getUser().getId())
                .totalPrice(getTotalPrice(cart))
                .totalQuantity(getTotalQuantity(cart))
                .cartItems(cart.getCartItems()
                        .stream()
                        .map(cartItemMapper::toCartItemResponse)
                        .toList()).build();
    }

    double getTotalPrice(Cart cart){
        return  cart.getCartItems().stream().map(item -> item.getItem().getPrice()*item.getNum()).mapToDouble(i -> i).sum() ;
    }

    int getTotalQuantity(Cart cart){
        return cart.getCartItems().stream().map(CartItem::getNum).mapToInt(i -> i).sum() ;
    }
}
