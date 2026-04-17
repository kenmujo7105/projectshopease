package com.EcommerceShop.Shop.mapper;

import com.EcommerceShop.Shop.entity.Feedback;
import com.EcommerceShop.Shop.dto.request.FeedbackCreateRequest;
import com.EcommerceShop.Shop.dto.request.FeedbackUpdateRequest;
import com.EcommerceShop.Shop.dto.response.FeedbackDTO;
import com.EcommerceShop.Shop.dto.response.FeedbackFullInfoResponse;
import com.EcommerceShop.Shop.dto.response.OrderItemResponse;
import com.EcommerceShop.Shop.dto.response.ProductResponse;
import com.EcommerceShop.Shop.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class FeedbackMapper {

    public abstract Feedback toFeedback(FeedbackCreateRequest request) ;

    public FeedbackFullInfoResponse toFeedbackResponse(Feedback feedback){
        FeedbackFullInfoResponse feedbackFullInfoResponse = new FeedbackFullInfoResponse() ;
        toFullInfo(feedbackFullInfoResponse,toFeedbackDTO(feedback));
        feedbackFullInfoResponse.setProductResponse( ProductResponse.builder()
                .id(feedback.getProduct().getId())
                .name(feedback.getProduct().getName())
                .imageUrl(feedback.getProduct().getImageUrl())
                .averageRate(feedback.getProduct().getAverageRate())
                .build());
        return feedbackFullInfoResponse;
    }
    public FeedbackDTO toFeedbackDTO(Feedback feedback){
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .comment(feedback.getComment())
                .rate(feedback.getRate())
                .orderItem(OrderItemResponse.builder()
                        .id(feedback.getOrderItem().getId())
                        .num(feedback.getOrderItem().getNum())
                        .build())
                .user(UserResponse.builder()
                        .id(feedback.getUser().getId())
                        .username(feedback.getUser().getEmail())
                        .build()).build();
    }
    public abstract void update(@MappingTarget Feedback feedback, FeedbackUpdateRequest request) ;


    @Mapping(target = "productResponse", ignore = true)
    protected abstract void toFullInfo(@MappingTarget FeedbackFullInfoResponse response ,FeedbackDTO dto);

}
