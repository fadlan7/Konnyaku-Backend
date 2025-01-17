package com.enigma.konyaku.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String userAccountId;
    private String shopId;
    private String username;
    private String token;
    private List<String> roles;
}
