package com.enigma.konyaku.dto.response.raja_ongkir.cost;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CostResultResponse {
    private String code;
    private String name;
    private List<CostServiceResponse> costs;
}
