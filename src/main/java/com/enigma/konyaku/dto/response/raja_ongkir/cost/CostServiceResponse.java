package com.enigma.konyaku.dto.response.raja_ongkir.cost;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CostServiceResponse {
    private String service;
    private String description;
    private List<CostServiceCostResponse> cost;
}
