package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.Shop;
import com.enigma.konyaku.entity.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String>, JpaSpecificationExecutor<Shop> {
    Optional<List<Shop>> findByActivityAndAvailability(Boolean activity, Boolean availability);
    Optional<Shop> findByUserAccount(UserAccount userAccount);

    @Query(value = "Select * From m_shop where user_account_id = :id", nativeQuery = true)
    Shop findShopByUserAccountId(@Param("id") String id);

    Shop findByUserAccount_Shop_Id(String shopId);
}
