package com.EcommerceShop.Shop.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class FeedbackDTO {
    private String id ;
    private String comment ;
    private Integer rate ;
    private UserResponse user ;
    private OrderItemResponse orderItem ;
}
