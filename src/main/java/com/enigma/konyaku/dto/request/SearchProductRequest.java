package com.enigma.konyaku.dto.request;

import com.enigma.konyaku.constant.ProductAvailability;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String name;
    private ProductAvailability status;
    private Integer maxPrice;
    private Integer minPrice;
}