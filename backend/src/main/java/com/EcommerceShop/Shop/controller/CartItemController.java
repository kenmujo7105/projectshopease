package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.dto.request.CartItemCreateRequest;
import com.EcommerceShop.Shop.dto.request.CartItemUpdateRequest;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.dto.response.CartItemResponse;
import com.EcommerceShop.Shop.service.CartItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart-item")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemController {
    CartItemService cartItemService;

    @PostMapping
    ApiResponseWrapper<CartItemResponse> createCartItem(@RequestBody CartItemCreateRequest request){
        return ApiResponseWrapper.<CartItemResponse>builder()
                .data(cartItemService.create(request)).build();
    }

    @PutMapping("/{itemId}")
    ApiResponseWrapper<CartItemResponse> updateCartItem(@PathVariable Long itemId, @RequestBody CartItemUpdateRequest request){
        return ApiResponseWrapper.<CartItemResponse>builder()
                .data(cartItemService.update(itemId, request)).build();
    }

    @DeleteMapping("/{itemId}")
    ApiResponseWrapper<?> deleteCartItem(@PathVariable Long itemId){
        cartItemService.deleteCartItem(itemId);
        return ApiResponseWrapper.builder()
                .code(200)
                .message("Item has been deleted")
                .build();
    }
//    @GetMapping("all")
//    ApiResponseWrapper<List<CartItemResponse>> getListCartOfUser(){
//        return ApiResponseWrapper.<List<CartItemResponse>>builder().build();
//    }
}
