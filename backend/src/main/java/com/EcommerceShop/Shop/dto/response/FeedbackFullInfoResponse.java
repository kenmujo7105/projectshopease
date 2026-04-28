package com.EcommerceShop.Shop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackFullInfoResponse extends FeedbackDTO {
    private ProductResponse productResponse ;
}
