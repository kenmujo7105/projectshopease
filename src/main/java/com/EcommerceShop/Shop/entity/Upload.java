package com.EcommerceShop.Shop.entity;

import com.EcommerceShop.Shop.enums.FileType;
import com.EcommerceShop.Shop.enums.OwnerType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "upload")
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id ;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "file_type")
    FileType fileType ;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "owner_type")
    OwnerType ownerType ;

    @Column(name = "owner_id")
    Long ownerId ;

    @Column(name = "url")
    String url ;

    @Column(name = "has_used")
    boolean used ;
}
