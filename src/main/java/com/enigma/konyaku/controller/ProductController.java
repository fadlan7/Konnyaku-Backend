package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.NewProductRequest;
import com.enigma.konyaku.dto.request.ProductDetailRequest;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.dto.response.ProductResponse;
import com.enigma.konyaku.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.API_PRODUCT)
public class ProductController {
    private final ProductService service;
    private final ObjectMapper objectMapper;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> create(
            @RequestPart(name = "product") String jsonProduct,
            @RequestParam(name = "thumbnail") MultipartFile thumbnail,
            @RequestParam(name = "images") MultipartFile[] images) {
        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();
        try {
            NewProductRequest request = objectMapper.readValue(jsonProduct, new TypeReference<>() {
            });

            int index = 0;

            for (ProductDetailRequest detailRequest : request.getDetails()) {
                detailRequest.setImage(images[index]);
                index++;
            }

            request.setThumbnail(thumbnail);

            ProductResponse productResponse = service.create(request);

            CommonResponse<ProductResponse> response = responseBuilder.statusCode(HttpStatus.CREATED.value())
                    .message("Successfully create new product")
                    .data(productResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            responseBuilder.message("Internal server error");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }
}
