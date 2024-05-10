package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.DeliveryFeeRequest;

public interface DeliveryService {
    Integer getDeliveryFee(DeliveryFeeRequest request);
}
