package com.enigma.konyaku.controller;

import com.enigma.konyaku.dto.request.DeliveryFeeRequest;
import com.enigma.konyaku.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/delivery")
public class DeliveryController {
    private final DeliveryService service;
    @PostMapping
    public ResponseEntity<Integer> getCost(@RequestBody DeliveryFeeRequest request) {
        Integer cost = service.getDeliveryFee(request);
        return ResponseEntity.ok(cost);
    }
}
