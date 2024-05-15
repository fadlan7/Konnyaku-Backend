package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM m_user WHERE user_account_id = ?1"
    )
    Optional<User> findByAccountId(String accountId);
}
