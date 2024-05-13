package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, String > {
    List<WishList> findWishListByUserId(String id);

    @Query(nativeQuery = true, value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM m_wish_list WHERE user_id = :userId AND product_id = :productId")
    boolean findingNemo(String userId, String productId);

    List<WishList> findWishListByUserIdAndProductId(String userId, String productId);

}
