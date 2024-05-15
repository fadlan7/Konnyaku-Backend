package com.enigma.konyaku.service;

import com.enigma.konyaku.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService extends UserDetailsService {
    UserAccount getByUserId(String id);
    UserAccount getContext();
}
