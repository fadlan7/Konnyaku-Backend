package com.enigma.konyaku.service;

import com.enigma.konyaku.dto.request.NewProductRequest;
import com.enigma.konyaku.dto.request.SearchProductRequest;
import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.UpdateProductRequest;
import com.enigma.konyaku.dto.response.ProductResponse;
import com.enigma.konyaku.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(NewProductRequest request);
    Page<ProductResponse> getAll(SearchProductRequest request);
    Product getProductById(String id);
    ProductResponse getById(String id);
    ProductResponse update(UpdateProductRequest request);
    void delete(String id);
}
