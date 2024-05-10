package com.enigma.konyaku.dto.response;

import com.enigma.konyaku.constant.ProductAvailability;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private ImageResponse thumbnail;
    private Integer weight;
    private ProductAvailability status;
    private List<ProductDetailResponse> details;

}
