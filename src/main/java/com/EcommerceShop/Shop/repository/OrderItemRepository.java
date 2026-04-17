package com.EcommerceShop.Shop.repository;

import com.EcommerceShop.Shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,String> {
    @Query(value = """
            SELECT oi.*
            FROM order_item oi
            JOIN product_detail dt ON dt.id = oi.item_id
            JOIN orders o ON o.id = oi.order_id
            WHERE o.user_id = :userId AND dt.product_id = :productId
            LIMIT 1
        """, nativeQuery = true)
    OrderItem findByUserAndProduct(@Param("userId") String userId,
                                @Param("productId") Long productId);
}
