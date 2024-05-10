package com.enigma.konyaku.dto.response.raja_ongkir.cost;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CostResponse {
    private RajaOngkirQueryResponse query;
    private RajaOngkirStatusResponse status;
    private RajaOngkirAddressResponse origin_details;
    private RajaOngkirAddressResponse destination_details;
    private List<CostResultResponse> results;
}
