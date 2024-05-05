package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.NewShopRequest;
import com.enigma.konyaku.dto.request.SearchShopRequest;
import com.enigma.konyaku.dto.request.ShopActivityRequest;
import com.enigma.konyaku.dto.request.UpdateShopRequest;
import com.enigma.konyaku.dto.response.ShopResponse;
import com.enigma.konyaku.entity.Shop;
import org.springframework.data.domain.Page;

public interface ShopService {
    Shop create(NewShopRequest request);
    ShopResponse getById(String id);
    Shop getShopById(String id);
    ShopResponse update(UpdateShopRequest request);
    Page<ShopResponse> getAll(SearchShopRequest request);
    ShopResponse changeActivity(ShopActivityRequest request);
}
