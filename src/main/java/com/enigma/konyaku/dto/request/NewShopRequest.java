package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewShopRequest {
    private String name;
    private String userAccountId;
    private String mobilePhoneNo;
    private AddressRequest address;
}
