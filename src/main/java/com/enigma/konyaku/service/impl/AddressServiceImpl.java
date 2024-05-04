package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.dto.request.AddressRequest;
import com.enigma.konyaku.dto.request.UpdateAddressRequest;
import com.enigma.konyaku.dto.response.AddressResponse;
import com.enigma.konyaku.entity.Address;
import com.enigma.konyaku.repository.AddressRepository;
import com.enigma.konyaku.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private  final AddressRepository addressRepository;
    @Override
    public Address create(AddressRequest request) {
        return addressRepository.saveAndFlush(
                Address.builder()
                        .street(request.getStreet())
                        .provinceId(request.getProvinceId())
                        .provinceName(request.getProvinceName())
                        .cityId(request.getCityId())
                        .cityName(request.getCityName())
                        .build()
        ) ;
    }

    @Override
    public Address getAddressById(String id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isEmpty()) throw new RuntimeException("Address not found");
        return address.get();
    }

    @Override
    public AddressResponse update(UpdateAddressRequest request) {
        Address address = getAddressById(request.getId());

        address.setStreet(request.getStreet());
        address.setCityId(request.getCityId());
        address.setCityName(request.getCityName());
        address.setProvinceId(request.getProvinceId());
        address.setProvinceName(request.getProvinceName());

        Address updatedAddress = addressRepository.saveAndFlush(address);

        return AddressResponse.builder()
                .id(updatedAddress.getId())
                .street(updatedAddress.getStreet())
                .cityId(updatedAddress.getCityId())
                .cityName(updatedAddress.getCityName())
                .provinceId(updatedAddress.getProvinceId())
                .provinceName(updatedAddress.getProvinceName())
                .build();
    }
}
