package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.service.FeedbackService;
import com.EcommerceShop.Shop.dto.request.FeedbackCreateRequest;
import com.EcommerceShop.Shop.dto.request.FeedbackUpdateRequest;
import com.EcommerceShop.Shop.dto.response.FeedbackDTO;
import com.EcommerceShop.Shop.dto.response.FeedbackFullInfoResponse;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService ;

    @PostMapping("/{productId}")
    ApiResponseWrapper<FeedbackFullInfoResponse> createFeedback(@PathVariable Long productId, @RequestBody FeedbackCreateRequest request){
        return ApiResponseWrapper.<FeedbackFullInfoResponse>builder()
                .data(feedbackService.createFeedback(productId,request)).build();
    }


    @GetMapping("/{productId}")
    ApiResponseWrapper<List<FeedbackDTO>> getFeedbackOfProduct(@PathVariable Long productId, Pageable pageable){
        return ApiResponseWrapper.<List<FeedbackDTO>>builder()
                .data(feedbackService.getListFeedbackByProduct(productId, pageable)).build();
    }
    @PutMapping("/{feedbackId}")
    ApiResponseWrapper<FeedbackFullInfoResponse> updateFeedback(
            @PathVariable String feedbackId,
            @RequestBody FeedbackUpdateRequest request
            ){
        return ApiResponseWrapper.<FeedbackFullInfoResponse>builder()
                .data(feedbackService.update(feedbackId,request)).build() ;
    }

    @DeleteMapping("/{feedbackId}")
    ApiResponseWrapper<?> delete(@PathVariable String feedbackId){
        feedbackService.deleteFeedback(feedbackId);
        return ApiResponseWrapper.builder()
                .message("Đã xóa thành công feedback").build();
    }
}
