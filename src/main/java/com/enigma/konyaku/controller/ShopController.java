package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.NewShopRequest;
import com.enigma.konyaku.dto.request.SearchShopRequest;
import com.enigma.konyaku.dto.request.ShopActivityRequest;
import com.enigma.konyaku.dto.request.UpdateShopRequest;
import com.enigma.konyaku.dto.response.AddressResponse;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.dto.response.PagingResponse;
import com.enigma.konyaku.dto.response.ShopResponse;
import com.enigma.konyaku.entity.Shop;
import com.enigma.konyaku.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiUrl.API_SHOP)
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;


    @GetMapping
    public ResponseEntity<CommonResponse<Page<ShopResponse>>> getAllShop(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "availability", required = false) Boolean availability,
            @RequestParam(name = "activity", required = false) Boolean activity
    ) {
        SearchShopRequest request = SearchShopRequest.builder()
                .page(page)
                .size(size)
                .name(name)
                .activity(activity)
                .availability(availability)
                .direction(direction)
                .sortBy(sortBy)
                .build();

        Page<ShopResponse> shopResponses = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(shopResponses.getTotalPages())
                .totalElement(shopResponses.getTotalElements())
                .page(shopResponses.getPageable().getPageNumber() + 1)
                .size(shopResponses.getPageable().getPageSize())
                .hasNext(shopResponses.hasNext())
                .hasPrevious(shopResponses.hasPrevious())
                .build();

        CommonResponse<Page<ShopResponse>> response = CommonResponse.<Page<ShopResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all shop")
                .data(shopResponses)
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<ShopResponse>> getById(@PathVariable String id) {
        ShopResponse shop = service.getById(id);

        CommonResponse<ShopResponse> response = CommonResponse.<ShopResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get shop")
                .data(shop)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ShopResponse>> changeStatus(
            @RequestBody ShopActivityRequest request
            ) {
        ShopResponse shopResponse = service.changeActivity(request);

        CommonResponse<ShopResponse> response = CommonResponse.<ShopResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update shop activity")
                .data(shopResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ShopResponse>> updateShop(
            @RequestBody UpdateShopRequest request
            ) {
        ShopResponse shopResponse = service.update(request);

        CommonResponse<ShopResponse> response = CommonResponse.<ShopResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update shop")
                .data(shopResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/userAccount/{userAccountId}")
    public ResponseEntity<CommonResponse<Shop>> getShopByUserAccountId(@PathVariable String userAccountId) {
        Shop shop = service.getShopByUserAccountId(userAccountId);

        CommonResponse<Shop> response = CommonResponse.<Shop>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully get shop")
                .data(shop)
                .build();

        return ResponseEntity.ok(response);
    }
}
