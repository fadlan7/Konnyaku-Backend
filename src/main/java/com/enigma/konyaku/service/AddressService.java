package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.AddressRequest;
import com.enigma.konyaku.dto.request.UpdateAddressRequest;
import com.enigma.konyaku.dto.response.AddressResponse;
import com.enigma.konyaku.entity.Address;

public interface AddressService {
    Address create(AddressRequest request);
    Address getAddressById(String id);
    AddressResponse update(UpdateAddressRequest request);
}
