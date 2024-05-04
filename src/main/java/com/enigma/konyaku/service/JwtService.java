package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.response.JwtClaims;
import com.enigma.konyaku.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
