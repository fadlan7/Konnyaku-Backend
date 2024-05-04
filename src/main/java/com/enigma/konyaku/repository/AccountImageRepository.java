package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.AccountImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountImageRepository extends JpaRepository<AccountImage, String> {
}
