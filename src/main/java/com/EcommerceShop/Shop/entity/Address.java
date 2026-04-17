package com.EcommerceShop.Shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id ;

    @Column(name = "province")
    String province ;

    @Column(name = "province_id")
    Long provinceId ;

    @Column(name = "district")
    String district ;

    @Column(name = "district_id")
    Long districtId ;

    @Column(name = "ward")
    String ward ;

    @Column(name = "ward_id")
    Long wardId ;



    @Column(name = "info")
    String info ;

    @Column(name = "receiver_name")
    String receiverName ;

    @Column(name = "phone")
    String phone ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user ;

    @Column(name = "is_default")
    boolean isDefault ;

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
