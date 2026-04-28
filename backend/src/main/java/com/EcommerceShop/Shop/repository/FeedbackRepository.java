package com.EcommerceShop.Shop.repository;

import com.EcommerceShop.Shop.entity.Feedback;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,String> {
    @Query(value = """
            SELECT *
            FROM feedback
            WHERE product_id = :productId
            """,
            countQuery = """
                    SELECT COUNT(*) FROM feedback WHERE product_id = :productId
                    """

            ,nativeQuery = true)
    Page<Feedback> getByProduct(@Param("productId") Long productId, Pageable pageable) ;
}
