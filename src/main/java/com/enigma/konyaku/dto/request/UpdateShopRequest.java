package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateShopRequest {
    private String id;
    private String name;
    private String mobilePhoneNo;
    private UpdateAddressRequest addressRequest;
    private Boolean availability;
}
