package com.enigma.konyaku.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListResponse {
    private String id;
    private ProductResponse product;
}
