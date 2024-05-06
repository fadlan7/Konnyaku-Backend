package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, String > {
}
