package com.enigma.konyaku.dto.response.raja_ongkir.cost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RajaOngkirQueryResponse {
    private String origin;
    private String destination;
    private Integer weight;
    private String courier;
}
