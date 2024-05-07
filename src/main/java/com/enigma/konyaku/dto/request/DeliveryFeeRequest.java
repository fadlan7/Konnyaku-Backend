package com.enigma.konyaku.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryFeeRequest {
    @JsonProperty("origin")
    private String originId;
    @JsonProperty("destination")
    private String destinationId;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("courier")
    private String courier;
}
