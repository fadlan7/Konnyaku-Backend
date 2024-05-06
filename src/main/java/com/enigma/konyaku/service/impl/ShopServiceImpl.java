package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.UserRole;
import com.enigma.konyaku.dto.request.NewShopRequest;
import com.enigma.konyaku.dto.request.SearchShopRequest;
import com.enigma.konyaku.dto.request.ShopActivityRequest;
import com.enigma.konyaku.dto.request.UpdateShopRequest;
import com.enigma.konyaku.dto.response.AddressResponse;
import com.enigma.konyaku.dto.response.ShopResponse;
import com.enigma.konyaku.entity.Address;
import com.enigma.konyaku.entity.Role;
import com.enigma.konyaku.entity.Shop;
import com.enigma.konyaku.entity.UserAccount;
import com.enigma.konyaku.repository.ShopRepository;
import com.enigma.konyaku.service.*;
import com.enigma.konyaku.specification.ShopSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository repository;
    private final UserAccountService accountService;
    private final AuthService authService;
    private final AddressService addressService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Shop create(NewShopRequest request) {
        UserAccount account = accountService.getByUserId(request.getUserAccountId());

        authService.updateRoles(account.getId());

        Address address = addressService.create(request.getAddress());

        Shop shop = repository.saveAndFlush(
                Shop.builder()
                        .name(request.getName())
                        .mobilePhoneNo(request.getMobilePhoneNo())
                        .address(address)
                        .availability(true)
                        .activity(true)
                        .userAccount(account)
                        .build()
        );
        return shop;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShopResponse getById(String id) {
        Shop shop = getShopById(id);
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(
                        AddressResponse.builder()
                                .street(shop.getAddress().getStreet())
                                .cityId(shop.getAddress().getCityId())
                                .cityName(shop.getAddress().getCityName())
                                .provinceId(shop.getAddress().getProvinceId())
                                .provinceName(shop.getAddress().getProvinceName())
                                .build()
                )
                .mobilePhoneNo(shop.getMobilePhoneNo())
                .activity(shop.getActivity())
                .availability(shop.getAvailability())
                .build();
    }
    @Transactional(rollbackFor = Exception.class)

    @Override
    public Shop getShopById(String id) {
        Optional<Shop> shop = repository.findById(id);
        if (shop.isEmpty()) throw new RuntimeException("Shop Not Found");
        return shop.get();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShopResponse update(UpdateShopRequest request) {
        Shop shop = getShopById(request.getId());

        if (request.getAddressRequest() != null) {
            AddressResponse response = addressService.update(request.getAddressRequest());
            Address address = Address.builder()
                    .id(response.getId())
                    .street(response.getStreet())
                    .cityName(response.getCityName())
                    .cityId(response.getCityId())
                    .provinceName(response.getProvinceName())
                    .provinceId(response.getProvinceId())
                    .build();
            shop.setAddress(address);
        }

        shop.setName(request.getName());
        shop.setMobilePhoneNo(request.getMobilePhoneNo());
        shop.setAvailability(request.getAvailability());

        shop = repository.saveAndFlush(shop);

        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .mobilePhoneNo(shop.getName())
                .availability(shop.getAvailability())
                .activity(shop.getActivity())
                .address(
                        AddressResponse.builder()
                                .id(shop.getAddress().getId())
                                .street(shop.getAddress().getStreet())
                                .provinceName(shop.getAddress().getProvinceName())
                                .provinceId(shop.getAddress().getProvinceId())
                                .cityName(shop.getAddress().getCityName())
                                .cityId(shop.getAddress().getCityId())
                                .build()
                )
                .build();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<ShopResponse> getAll(SearchShopRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()),request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Specification<Shop> specification = ShopSpecification.getSpecification(request);

        return repository.findAll(specification, pageable).map(
                shop -> {
                    return ShopResponse.builder()
                            .id(shop.getId())
                            .name(shop.getName())
                            .mobilePhoneNo(shop.getMobilePhoneNo())
                            .address(
                                    AddressResponse.builder()
                                            .id(shop.getAddress().getId())
                                            .street(shop.getAddress().getStreet())
                                            .cityId(shop.getAddress().getCityId())
                                            .cityName(shop.getAddress().getCityName())
                                            .provinceId(shop.getAddress().getProvinceId())
                                            .provinceName(shop.getAddress().getProvinceName())
                                            .build()
                            )
                            .activity(shop.getActivity())
                            .availability(shop.getAvailability())
                            .build();
                }
        );
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShopResponse changeActivity(ShopActivityRequest request) {
        Shop shop = getShopById(request.getId());
        shop.setActivity(request.getActivity());

        repository.saveAndFlush(shop);

        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .mobilePhoneNo(shop.getName())
                .availability(shop.getAvailability())
                .activity(shop.getActivity())
                .address(
                        AddressResponse.builder()
                                .id(shop.getAddress().getId())
                                .street(shop.getAddress().getStreet())
                                .provinceName(shop.getAddress().getProvinceName())
                                .provinceId(shop.getAddress().getProvinceId())
                                .cityName(shop.getAddress().getCityName())
                                .cityId(shop.getAddress().getCityId())
                                .build()
                )
                .build();
    }
}
