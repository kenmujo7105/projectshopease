package com.EcommerceShop.Shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "category")
public class Category  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    Long id;

    @Column(name = "name")
    String name ;

    @Column(name = "description")
    String description ;

    @Column(name = "urlImage")
    String urlImage ;
    //Link
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductCategory> productCategories ;
}
