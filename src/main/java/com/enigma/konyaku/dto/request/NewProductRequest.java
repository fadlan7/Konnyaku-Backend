package com.enigma.konyaku.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductRequest {
    private String name;
    private String description;
    private Integer weight;
    private String shopId;
    private List<ProductDetailRequest> details;

}
