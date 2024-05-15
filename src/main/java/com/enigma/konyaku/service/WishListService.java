package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.WishListRequest;
import com.enigma.konyaku.dto.response.WishListResponse;
import com.enigma.konyaku.entity.WishList;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishListService {
    WishList create(WishListRequest request);
    List<WishListResponse> getAll(String id);
    void delete(String id);
    boolean checkExisting(String userAccId, String productId);
}
