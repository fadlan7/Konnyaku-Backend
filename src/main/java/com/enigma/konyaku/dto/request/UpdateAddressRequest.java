package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAddressRequest {
    private String id;
    private String street;
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
}
