package com.enigma.konyaku.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String id;
    private String street;
    private String cityId;
    private String cityName;
    private String provinceId;
    private String provinceName;
}
