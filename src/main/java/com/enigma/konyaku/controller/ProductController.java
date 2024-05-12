package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;

import com.enigma.konyaku.constant.ResponseMessage;
import com.enigma.konyaku.dto.request.*;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.dto.response.PagingResponse;
import com.enigma.konyaku.dto.response.ProductResponse;
import com.enigma.konyaku.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.API_PRODUCT)
public class ProductController {
    private final ProductService service;
    private final ObjectMapper objectMapper;
    private final GenericResponseService responseBuilder;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> create(
            @RequestPart(name = "product") String jsonProduct,
            @RequestParam(name = "thumbnail") MultipartFile thumbnail,
            @RequestParam(name = "images") List<MultipartFile> images) {
        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();
        try {
            NewProductRequest request = objectMapper.readValue(jsonProduct, new TypeReference<>() {
            });

            int index = 0;

            for (ProductDetailRequest detailRequest : request.getDetails()) {
                detailRequest.setImage(images.get(index));
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

    @GetMapping(path = "/shop")
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "shopId", required = true) String shopId
    ) {
        SearchProductByShopRequest request = SearchProductByShopRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .q(q)
                .shopId(shopId)
                .build();
        Page<ProductResponse> products = service.getAllByShop(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(products.getTotalPages())
                .totalElement(products.getTotalElements())
                .page(products.getPageable().getPageNumber() + 1)
                .size(products.getPageable().getPageSize())
                .hasNext(products.hasNext())
                .hasPrevious(products.hasPrevious())
                .build();

        CommonResponse<List<ProductResponse>> response = CommonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all products")
                .data(products.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> getById(@PathVariable("id") String id) {
        ProductResponse productResponse = service.getById(id);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get products")
                .data(productResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(
            @RequestParam(name = "product") String jsonProduct,
            @RequestParam(name = "thumbnail") MultipartFile thumbnail,
            @RequestParam(name = "images") List<MultipartFile>  images
    ) throws JsonProcessingException {
        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();

        UpdateProductRequest request = objectMapper.readValue(jsonProduct, new TypeReference<>() {
        });

        request.setThumbnail(thumbnail);

        int index = 0;

        for (UpdateProductDetailRequest detailRequest : request.getDetails()) {
            detailRequest.setImage(images.get(index));
            index++;
        }

        ProductResponse productResponse = service.update(request);

        CommonResponse<ProductResponse> response = responseBuilder.statusCode(HttpStatus.CREATED.value())
                .message("Successfully create new product")
                .data(productResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable("id") String id) {
        service.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }
}
