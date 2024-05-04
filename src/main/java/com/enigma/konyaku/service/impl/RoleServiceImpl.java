package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.UserRole;
import com.enigma.konyaku.entity.Role;
import com.enigma.konyaku.repository.RoleRepository;
import com.enigma.konyaku.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole role) {
        return repository.findByRole(role)
                .orElseGet(() -> repository.saveAndFlush(Role.builder().role(role).build()));
    }
}
