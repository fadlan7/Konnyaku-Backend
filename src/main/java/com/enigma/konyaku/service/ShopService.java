package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.NewShopRequest;
import com.enigma.konyaku.dto.request.SearchShopRequest;
import com.enigma.konyaku.dto.request.ShopActivityRequest;
import com.enigma.konyaku.dto.request.UpdateShopRequest;
import com.enigma.konyaku.dto.response.ShopResponse;
import com.enigma.konyaku.entity.Shop;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


public interface ShopService {
    Shop create(Shop shop);
    ShopResponse getById(String id);
    Shop getShopById(String id);
    ShopResponse update(UpdateShopRequest request);
    Page<ShopResponse> getAll(SearchShopRequest request);
    ShopResponse changeActivity(ShopActivityRequest request);
    Shop getShopByUserAccountId(String userAccountId);
    String getShopId(String userAccountId);
}
