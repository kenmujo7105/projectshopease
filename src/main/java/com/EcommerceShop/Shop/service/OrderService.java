package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.OrderItemRequest;
import com.EcommerceShop.Shop.dto.request.OrderRequest;
import com.EcommerceShop.Shop.dto.request.ShippingFeeRequest;
import com.EcommerceShop.Shop.dto.request.UpdateStatusRequest;
import com.EcommerceShop.Shop.dto.response.OrderNotify;
import com.EcommerceShop.Shop.dto.response.OrderResponse;
import com.EcommerceShop.Shop.dto.response.PreviewOrderResponse;
import com.EcommerceShop.Shop.entity.OrderItem;
import com.EcommerceShop.Shop.entity.Orders;
import com.EcommerceShop.Shop.entity.ProductDetail;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.enums.OrderItemStatus;
import com.EcommerceShop.Shop.enums.OrderStatus;
import com.EcommerceShop.Shop.exception.AppException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface OrderService {

    OrderResponse createOrderItem(OrderRequest request) ;

    PreviewOrderResponse preview(OrderRequest request) ;

    void deleteOrderItem(String orderItemId) ;

    OrderResponse updateOrderStatus(String orderId, UpdateStatusRequest request);

    List<OrderStatus> getListOrderStatus() ;

    OrderResponse getOrder(String orderId);

    List<OrderResponse> getAllOrder();
}
