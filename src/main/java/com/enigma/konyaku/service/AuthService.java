package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.AuthRequest;
import com.enigma.konyaku.dto.request.RegisterRequest;
import com.enigma.konyaku.dto.request.RegisterShopRequest;
import com.enigma.konyaku.dto.response.LoginResponse;
import com.enigma.konyaku.dto.response.RegisterResponse;
import com.enigma.konyaku.dto.response.RegisterShopResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    RegisterShopResponse registerShop(RegisterShopRequest request);
    RegisterResponse registerAdmin(RegisterRequest request);
    LoginResponse login(AuthRequest request);
    boolean validateToken();
    RegisterResponse updateRoles(String id);
}
