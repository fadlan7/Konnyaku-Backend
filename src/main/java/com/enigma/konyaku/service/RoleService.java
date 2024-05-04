package com.enigma.konyaku.service;

import com.enigma.konyaku.constant.UserRole;
import com.enigma.konyaku.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole role);
}
