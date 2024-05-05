package com.enigma.konyaku.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponse {
    private String id;
    private String name;
    private String description;
    private Integer price;
    private ImageResponse image;
}
