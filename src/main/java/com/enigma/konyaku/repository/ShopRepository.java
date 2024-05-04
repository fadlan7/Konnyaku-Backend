package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String>, JpaSpecificationExecutor<Shop> {
    Optional<List<Shop>> findByActivityAndAvailability(Boolean activity, Boolean availability);
}
