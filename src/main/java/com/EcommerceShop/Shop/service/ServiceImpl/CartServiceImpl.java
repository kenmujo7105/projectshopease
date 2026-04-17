package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.mapper.CartMapper;
import com.EcommerceShop.Shop.repository.CartRepository;
import com.EcommerceShop.Shop.dto.response.CartResponse;
import com.EcommerceShop.Shop.entity.Cart;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.repository.UserRepository;
import com.EcommerceShop.Shop.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    CartRepository cartRepository ;
    UserRepository userRepository ;
    CartMapper cartMapper ;

    public Cart create(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;

        User user = userRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        Cart cart = Cart.builder()
                .cartItems(new ArrayList<>())
                .user(user)
                .build();
        user.setCarts(cart);
        return cartRepository.save(cart) ;
    }

    public CartResponse getCartByUser(String username){
        User user = userRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        return cartMapper.toCartResponse(cartRepository.findByUser(user)) ;
    }
}