package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.AuthRequest;
import com.enigma.konyaku.dto.request.RegisterRequest;
import com.enigma.konyaku.dto.request.RegisterShopRequest;
import com.enigma.konyaku.dto.response.CommonResponse;
import com.enigma.konyaku.dto.response.LoginResponse;
import com.enigma.konyaku.dto.response.RegisterResponse;
import com.enigma.konyaku.dto.response.RegisterShopResponse;
import com.enigma.konyaku.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = ApiUrl.API_AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/register/user",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> registerUser(
            @RequestPart(name = "registration") String jsonRegistration,
            @RequestPart(name = "images") MultipartFile[] images
            ) {
        CommonResponse.CommonResponseBuilder<RegisterResponse> responseBuilder = CommonResponse.builder();
        try {
            RegisterRequest request = objectMapper.readValue(jsonRegistration, new TypeReference<>() {
            });
            request.setImages(List.of(images));

            RegisterResponse register = authService.register(request);
            CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully Registered")
                    .data(register)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            responseBuilder.message("Internal server error");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }

    @PostMapping(
            path = "/register-shop",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterShopResponse>> registerShop(@RequestBody RegisterShopRequest request){
        RegisterShopResponse register = authService.registerShop(request);
        CommonResponse<RegisterShopResponse> response = CommonResponse.<RegisterShopResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully register shop")
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/role")
    public ResponseEntity<CommonResponse<RegisterResponse>> updateRoles(@RequestBody String id){
        RegisterResponse registerResponse = authService.updateRoles(id);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update roles")
                .data(registerResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register/admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> registerAdmin(@RequestBody RegisterRequest request) {
        RegisterResponse register = authService.registerAdmin(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully Registered")
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Login")
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken() {
        boolean valid = authService.validateToken();
        if (valid) {
            CommonResponse<String> response = CommonResponse.<String>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully got data")
                    .build();
            return ResponseEntity.ok(response);
        } else {
            CommonResponse<String> response = CommonResponse.<String>builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid JWT")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
