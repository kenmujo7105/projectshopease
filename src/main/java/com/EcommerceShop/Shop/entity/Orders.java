package com.EcommerceShop.Shop.entity;

import com.EcommerceShop.Shop.enums.OrderItemStatus;
import com.EcommerceShop.Shop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Orders  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user ;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    OrderStatus status ;

    @Column(name = "create_at")
    Date createdAt ;

    Double total ;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL,orphanRemoval = true)
    List<OrderItem> orderItems;


    public List<OrderItem> getItemByStatus(OrderItemStatus status){
        return orderItems.stream().filter(orderItem -> status.equals(orderItem.getStatus())).toList() ;
    }
}
