package com.enigma.konyaku.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String mobilePhoneNo;
    private AddressRequest addressRequest;
    private List<MultipartFile> images;
}
