package com.EcommerceShop.Shop.controller;

import com.EcommerceShop.Shop.dto.request.UploadRequest;
import com.EcommerceShop.Shop.dto.response.UploadResponse;
import com.EcommerceShop.Shop.dto.ApiResponseWrapper;
import com.EcommerceShop.Shop.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@RequestMapping("/upload")
public class UploadController {
    @Autowired
    UploadService uploadService ;
    @PostMapping(value = "/upload")
    ApiResponseWrapper<UploadResponse> upload(@Valid @RequestPart("file") MultipartFile file ,
                                 @Valid @RequestPart("fileType") String fileType ,
                                 @Valid @RequestPart("ownerType") String ownerType ,
                                 @Valid @RequestPart("ownerId") Long ownerId ) throws IOException {
        UploadRequest request = UploadRequest.builder()
                .file(file)
                .fileType(fileType)
                .ownerId(ownerId)
                .ownerType(ownerType).build() ;
        return ApiResponseWrapper.<UploadResponse>builder()
                .data(uploadService.upload(request.getFile(), request))
                   .build();
    }
}
