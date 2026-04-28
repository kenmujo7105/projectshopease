package com.EcommerceShop.Shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blacklist_token")
public class BlacklistToken  implements Serializable {
    @Id
    @Column(name = "id")
    String token ;

    @Column(name = "expires_at")
    Date expired_date ;
}
