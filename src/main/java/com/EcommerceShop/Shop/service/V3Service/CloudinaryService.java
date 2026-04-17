package com.EcommerceShop.Shop.service.V3Service;

import com.EcommerceShop.Shop.config.CloudinaryConfig;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary ;

    public CloudinaryService(CloudinaryConfig config) {
        this.cloudinary = new Cloudinary(config.getLink());
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
            // Tải file lên Cloudinary
            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder, // Lưu vào thư mục chỉ định
                    "upload_preset", "e-commerce-web", // Tên preset đã tạo
                    "resource_type", "auto" // Tự động nhận diện loại file (image, video,...)
            ));

            // Lấy URL công khai của file
            String publicUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            System.out.println("File stored at: " + publicId); // Đường dẫn: folder/file-id
            return publicUrl;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CLOUDINARY_ERROR,e.getMessage()) ;
        }
    }
}
