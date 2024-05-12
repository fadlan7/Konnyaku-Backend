package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, String > {
    List<WishList> findWishListByUserId(String id);
}
