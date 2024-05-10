package com.enigma.konyaku.dto.response.raja_ongkir.cost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RajaOngkirStatusResponse {
    private Integer code;
    private String description;
}
