package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.dto.request.DeliveryFeeRequest;
import com.enigma.konyaku.dto.response.raja_ongkir.cost.RajaOngkirCostResponse;
import com.enigma.konyaku.service.DeliveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.util.Map;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final ObjectMapper mapper;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String BASE_URL;
    public DeliveryServiceImpl(
            RestClient restClient,
            @Value("b6d8c64a420e48028977d4dc54fe2024") String secretKey,
            @Value("https://api.rajaongkir.com/starter") String baseUrl,
            ObjectMapper mapper
    ) {
        this.restClient = restClient;
        this.mapper = mapper;
        this.BASE_URL = baseUrl;
        this.SECRET_KEY = secretKey;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer getDeliveryFee(DeliveryFeeRequest request) {

        MultiValueMap<String , Object> form = new LinkedMultiValueMap<>();
        form.add("destination",request.getDestinationId());
        form.add("origin", request.getDestinationId());
        form.add("courier", request.getCourier());
        form.add("weight", request.getWeight());

        ResponseEntity<String> response = restClient.post()
                .uri(BASE_URL + "/cost")
                .header("key", SECRET_KEY)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve().toEntity(String.class);

        RajaOngkirCostResponse body = null;
        try {
            body = mapper.readValue(response.getBody(), RajaOngkirCostResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return body.getRajaongkir().getResults().get(0).getCosts().get(0).getCost().get(0).getValue();
    }
}
