package com.enigma.konyaku.dto.response.raja_ongkir.cost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RajaOngkirAddressResponse {
    private String city_id;
    private String province_id;
    private String province;
    private String type;
    private String city_name;
    private String postal_code;
}
