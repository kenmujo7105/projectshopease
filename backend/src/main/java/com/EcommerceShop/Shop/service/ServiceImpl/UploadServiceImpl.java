package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.repository.BrandRepository;
import com.EcommerceShop.Shop.repository.CategoryRepository;
import com.EcommerceShop.Shop.repository.ProductRepository;
import com.EcommerceShop.Shop.dto.request.UploadRequest;
import com.EcommerceShop.Shop.dto.response.UploadResponse;
import com.EcommerceShop.Shop.enums.OwnerType;
import com.EcommerceShop.Shop.entity.Upload;
import com.EcommerceShop.Shop.enums.FileType;
import com.EcommerceShop.Shop.repository.UploadRepository;
import com.EcommerceShop.Shop.service.UploadService;
import com.EcommerceShop.Shop.service.V3Service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    UploadRepository uploadRepository ;
    ProductRepository productRepository;
    CategoryRepository categoryRepository ;
    BrandRepository brandRepository ;
    @Autowired
    CloudinaryService cloudinaryService ;

    @PreAuthorize("#request.ownerType.toLowerCase() != 'user' ?  hasRole('ADMIN') : true")
    public UploadResponse upload(MultipartFile filePart , UploadRequest request) throws IOException {
        var folder = request.getFileType().toLowerCase() ;
        var url = cloudinaryService.uploadFile(filePart,folder) ;
        FileType fileType = FileType.valueOf(request.getFileType().toUpperCase()) ;
        OwnerType ownerType = OwnerType.valueOf(request.getOwnerType().toUpperCase()) ;
        boolean valid = validateOwner(request.getOwnerId(), ownerType) ;
        Upload upload = Upload.builder()
                .ownerId(request.getOwnerId())
                .fileType(fileType)
                .ownerType(ownerType)
                .url(url)
                .build();
        upload = uploadRepository.save(upload) ;
        return UploadResponse.builder()
                .ownerType(ownerType.name())
                .ownerId(upload.getOwnerId())
                .uploadId(upload.getId())
                .url(url).build();
    }

    private boolean validateOwner(Long id, OwnerType ownerType) {
        JpaRepository<?, Long> repository = switch (ownerType) {
            case PRODUCT -> productRepository;
            case CATEGORY -> categoryRepository;
            case BRAND -> brandRepository;
        };
        return ownerType.validate(id, repository);
    }

}
