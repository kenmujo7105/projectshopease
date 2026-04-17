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
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "brand")
public class Brand  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id ;

    @Column(name = "name")
    String name ;

    @Column(name = "description")
    String description ;

    @Column(name = "logo")
    String logoUrl ;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = false)
    List<Product> productList ;

}
