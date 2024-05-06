package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.WishListRequest;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.entity.WishList;
import com.enigma.konyaku.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.API_WISH_LIST)
public class WishListController {
    private final WishListService service;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<WishList>> addWishList(@RequestBody WishListRequest request) {
        WishList wishList = service.create(request);

        CommonResponse<WishList> response = CommonResponse.<WishList>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully add new wish list item")
                .data(wishList)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping
    public ResponseEntity<CommonResponse<Page<WishList>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {
        SearchRequest request = SearchRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .build();

        Page<WishList> wishLists = service.getAll(request);

        CommonResponse<Page<WishList>> response = CommonResponse.<Page<WishList>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all data")
                .data(wishLists)
                .build();

        return ResponseEntity.ok(response);
    }
}
