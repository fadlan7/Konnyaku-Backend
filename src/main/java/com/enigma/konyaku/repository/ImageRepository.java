package com.enigma.konyaku.repository;

import com.enigma.konyaku.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    Optional<Image> findByContentType(String contentType);
}
