package com.EcommerceShop.Shop.service;

import com.EcommerceShop.Shop.dto.request.UploadRequest;
import com.EcommerceShop.Shop.dto.response.UploadResponse;
import com.EcommerceShop.Shop.entity.Upload;
import com.EcommerceShop.Shop.enums.FileType;
import com.EcommerceShop.Shop.enums.OwnerType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    UploadResponse upload(MultipartFile filePart , UploadRequest request) throws IOException ;
}
