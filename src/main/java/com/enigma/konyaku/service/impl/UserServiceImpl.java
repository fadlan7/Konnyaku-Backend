package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.UpdateUserRequest;
import com.enigma.konyaku.dto.response.ImageResponse;
import com.enigma.konyaku.dto.response.UserResponse;
import com.enigma.konyaku.entity.User;
import com.enigma.konyaku.repository.UserRepository;
import com.enigma.konyaku.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public UserResponse getById(String id) {
        User user = getUserById(id);
        return UserResponse.builder()
                .id(user.getId())
                .identificationImages(
                        user.getIdentificationImages()
                                .stream().map((accountImage ->
                                        ImageResponse.builder()
                                                .name(accountImage.getImage().getName())
                                                .url(ApiUrl.API_MENU_IMAGE_DOWNLOAD + accountImage.getImage().getId())
                                                .build()
                                        )).toList())
                .build();
    }

    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new RuntimeException("User not found");
        return user.get();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @Override
    public Page<UserResponse> getAll(SearchRequest request) {
        return null;
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
