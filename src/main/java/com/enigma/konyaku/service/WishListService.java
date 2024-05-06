package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.WishListRequest;
import com.enigma.konyaku.entity.WishList;
import org.springframework.data.domain.Page;

public interface WishListService {
    WishList create(WishListRequest request);
    Page<WishList> getAll(SearchRequest request);
}
