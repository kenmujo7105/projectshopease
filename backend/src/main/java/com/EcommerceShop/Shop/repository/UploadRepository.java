package com.EcommerceShop.Shop.repository;

import com.EcommerceShop.Shop.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload,Long> {
}
