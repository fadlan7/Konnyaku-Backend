package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.UpdateUserRequest;
import com.enigma.konyaku.dto.response.UserResponse;
import com.enigma.konyaku.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    User create(User user);
    UserResponse getById(String id);
    User getUserById(String id);
    Page<UserResponse> getAll(SearchRequest request);
    UserResponse update(UpdateUserRequest request);
    User findByAccountId(String id);
    void delete(String id);
}
