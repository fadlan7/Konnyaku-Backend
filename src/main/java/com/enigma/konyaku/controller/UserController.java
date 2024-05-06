package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.UpdateUserRequest;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.dto.response.ShopResponse;
import com.enigma.konyaku.dto.response.UserResponse;
import com.enigma.konyaku.entity.User;
import com.enigma.konyaku.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.API_USER)
public class UserController {
    private final UserService service;
    @Transactional(rollbackFor = Exception.class)
    @GetMapping
    public ResponseEntity<CommonResponse<Page<UserResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchRequest request = SearchRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .name(name)
                .build();

        Page<UserResponse> userResponses = service.getAll(request);

        CommonResponse<Page<UserResponse>> response = CommonResponse.<Page<UserResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .data(userResponses)
                .message("Successfully get all user")
                .build();

        return ResponseEntity.ok(response);
    }
    @Transactional(rollbackFor = Exception.class)
    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<UserResponse>> getById(@PathVariable String id) {
        UserResponse userResponse = service.getById(id);

        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get user")
                .data(userResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @PutMapping
    public ResponseEntity<CommonResponse<UserResponse>> update(@RequestBody UpdateUserRequest request) {
        UserResponse userResponse = service.update(request);

        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update user")
                .data(userResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
