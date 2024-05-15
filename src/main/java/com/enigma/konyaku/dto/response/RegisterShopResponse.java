package com.enigma.konyaku.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterShopResponse {
    private String id;
    private String name;
    private List<String> roles;
}
