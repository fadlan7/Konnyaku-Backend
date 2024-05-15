package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterShopRequest {
    private String userAccountId;
    private String name;
    private String mobilePhoneNo;
    private AddressRequest address;
}
