package com.enigma.konyaku.repository;

import com.enigma.konyaku.constant.UserRole;
import com.enigma.konyaku.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
