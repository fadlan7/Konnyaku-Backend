package com.enigma.konyaku.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String mobilePhoneNo;
    private AddressResponse address;
    private List<ImageResponse> identificationImages;
}
