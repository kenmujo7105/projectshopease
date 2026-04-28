package com.EcommerceShop.Shop.dto.info;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String email;
    @Size(min = 8, message = "Password must have min 8")
    String password ;
    String firstName ;
    String lastName ;
    Date dob ;
}
