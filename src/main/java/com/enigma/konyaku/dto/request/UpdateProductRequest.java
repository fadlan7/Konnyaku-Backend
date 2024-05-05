package com.enigma.konyaku.dto.request;

import com.enigma.konyaku.constant.ProductAvailability;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    private String id;
    private String name;
    private String description;
    private ProductAvailability status;
}
