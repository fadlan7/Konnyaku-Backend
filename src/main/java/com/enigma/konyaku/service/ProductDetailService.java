package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.ProductDetailRequest;
import com.enigma.konyaku.dto.request.UpdateProductDetailRequest;
import com.enigma.konyaku.entity.ProductDetail;

import java.util.List;

public interface ProductDetailService {
    List<ProductDetail> create(List<ProductDetail> details);
    ProductDetail update(UpdateProductDetailRequest request);
    ProductDetail getDetailById(String id);

}
