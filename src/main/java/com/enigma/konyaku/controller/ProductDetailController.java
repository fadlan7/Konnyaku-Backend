package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.UpdateProductDetailRequest;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.dto.response.ImageResponse;
import com.enigma.konyaku.dto.response.ProductDetailResponse;
import com.enigma.konyaku.entity.ProductDetail;
import com.enigma.konyaku.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.API_PRODUCT_DETAIL)
public class ProductDetailController {
    private final ProductDetailService service;

    @Transactional(rollbackFor = Exception.class)
    @PutMapping
    public ResponseEntity<CommonResponse<ProductDetailResponse>> updateDetail(
            @RequestBody UpdateProductDetailRequest request
    ) {
        ProductDetail detail = service.update(request);
        ProductDetailResponse detailResponse = ProductDetailResponse.builder()
                .id(detail.getId())
                .name(detail.getName())
                .image(ImageResponse.builder()
                        .name(detail.getImage().getName())
                        .url(ApiUrl.API_IMAGE_DOWNLOAD + detail.getImage().getId())
                        .build())
                .price(detail.getPrice())
                .description(detail.getDescription())
                .build();

        CommonResponse<ProductDetailResponse> response = CommonResponse.<ProductDetailResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update data")
                .data(detailResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}