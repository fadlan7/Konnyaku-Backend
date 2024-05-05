package com.enigma.konyaku.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponse {
    private String id;
    private String name;
    private String mobilePhoneNo;
    private AddressResponse address;
    private Boolean availability;
    private Boolean activity;
}
