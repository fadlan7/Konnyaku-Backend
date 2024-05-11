package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.constant.ResponseMessage;
import com.enigma.konyaku.dto.request.AddressRequest;
import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.UpdateUserRequest;
import com.enigma.konyaku.dto.response.AddressResponse;
import com.enigma.konyaku.dto.response.ImageResponse;
import com.enigma.konyaku.dto.response.UserResponse;
import com.enigma.konyaku.entity.Address;
import com.enigma.konyaku.entity.User;
import com.enigma.konyaku.repository.UserRepository;
import com.enigma.konyaku.service.UserService;
import com.enigma.konyaku.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User create(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User findByAccountId(String id) {
        return userRepository.findByAccountId(id).orElseThrow(() -> new RuntimeException(ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse getById(String id) {
        User user = getUserById(id);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .mobilePhoneNo(user.getMobilePhoneNo())
                .address(
                        AddressResponse.builder()
                                .id(user.getAddress().getId())
                                .provinceName(user.getAddress().getProvinceName())
                                .provinceId(user.getAddress().getProvinceId())
                                .cityName(user.getAddress().getCityName())
                                .cityId(user.getAddress().getCityId())
                                .build()
                )
                .identificationImages(
                        user.getIdentificationImages()
                                .stream().map((accountImage ->
                                        ImageResponse.builder()
                                                .name(accountImage.getImage().getName())
                                                .url(ApiUrl.API_IMAGE_DOWNLOAD + accountImage.getImage().getId())
                                                .build()
                                )).toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new RuntimeException("User not found");
        return user.get();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<UserResponse> getAll(SearchRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Specification<User> specification = UserSpecification.getSpecification(request);

        return userRepository.findAll(specification, pageable).map(
                user -> {
                    return UserResponse.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .mobilePhoneNo(user.getMobilePhoneNo())
                            .address(
                                    AddressResponse.builder()
                                            .id(user.getAddress().getId())
                                            .provinceName(user.getAddress().getProvinceName())
                                            .provinceId(user.getAddress().getProvinceId())
                                            .cityName(user.getAddress().getCityName())
                                            .cityId(user.getAddress().getCityId())
                                            .build()
                            )
                            .identificationImages(user.getIdentificationImages()
                                    .stream().map((accountImage -> ImageResponse.builder()
                                            .name(accountImage.getImage().getName())
                                            .url(ApiUrl.API_IMAGE_DOWNLOAD + accountImage.getImage().getId())
                                            .build())).toList())
                            .build();
                }
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse update(UpdateUserRequest request) {
        User user = getUserById(request.getId());

        AddressRequest addressRequest = request.getAddressRequest();
        Address address = user.getAddress();

        address.setProvinceName(addressRequest.getProvinceName());
        address.setProvinceId(addressRequest.getProvinceId());
        address.setCityName(addressRequest.getCityName());
        address.setCityId(address.getCityId());

        user.setActivity(request.getActivity());
        user.setMobilePhoneNo(request.getMobilePhoneNo());
        user.setName(request.getName());
        user.setAddress(address);

        user = userRepository.saveAndFlush(user);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .mobilePhoneNo(user.getMobilePhoneNo())
                .address(
                        AddressResponse.builder()
                                .street(user.getAddress().getStreet())
                                .cityId(user.getAddress().getCityId())
                                .cityName(user.getAddress().getCityName())
                                .provinceId(user.getAddress().getProvinceId())
                                .provinceName(user.getAddress().getProvinceName())
                                .build()
                )
                .identificationImages(
                        user.getIdentificationImages().stream().map(
                                image -> ImageResponse.builder()
                                        .name(image.getImage().getName())
                                        .url(ApiUrl.API_IMAGE_DOWNLOAD + image.getImage().getId())
                                        .build()
                        ).toList()
                )
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
