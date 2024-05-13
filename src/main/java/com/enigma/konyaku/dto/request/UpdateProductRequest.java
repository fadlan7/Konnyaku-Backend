package com.enigma.konyaku.dto.request;

import com.enigma.konyaku.constant.ProductAvailability;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    private String id;
    private String name;
    private String description;
    private Integer weight;
    private ProductAvailability status;
    private MultipartFile thumbnail;
    private List<UpdateProductDetailRequest> details;
}
