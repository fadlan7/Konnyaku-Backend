package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRequest {
    private String service;
    private DeliveryFeeRequest deliveryFeeRequest;
}
