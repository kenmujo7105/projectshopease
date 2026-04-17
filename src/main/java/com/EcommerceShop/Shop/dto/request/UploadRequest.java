package com.EcommerceShop.Shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UploadRequest {
    @NotNull(message = "file is required")
    private MultipartFile file ;
    @NotBlank(message = "fileType is required")
    private String fileType ;
    @NotBlank(message = "ownerType is required")
    private String ownerType ;
    @NotBlank(message = "ownerId is required")
    private Long ownerId ;
}
