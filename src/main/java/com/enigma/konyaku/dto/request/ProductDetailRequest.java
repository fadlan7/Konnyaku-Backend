package com.enigma.konyaku.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailRequest {
    private String name;
    private String description;
    private Integer weight;
    private Integer price;
    private MultipartFile image;
}
