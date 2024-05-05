package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.dto.request.UpdateProductDetailRequest;
import com.enigma.konyaku.entity.Image;
import com.enigma.konyaku.entity.ProductDetail;
import com.enigma.konyaku.repository.ProductDetailRepository;
import com.enigma.konyaku.service.ImageService;
import com.enigma.konyaku.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductDetailRepository repository;
    private final ImageService imageService;
    @Override
    public List<ProductDetail> create(List<ProductDetail> details) {
        return repository.saveAllAndFlush(details);
    }

    @Override
    public ProductDetail getDetailById(String id) {
        Optional<ProductDetail> detail = repository.findById(id);
        if (detail.isEmpty()) throw new RuntimeException("Product Detail Not Found");
        return detail.get();
    }

    @Override
    public ProductDetail update(UpdateProductDetailRequest request) {
        ProductDetail detail = getDetailById(request.getId());

        if (request.getImage() != null) {
            Image image = imageService.create(request.getImage());
            detail.setImage(image);
        }

        detail.setName(request.getName());
        detail.setDescription(request.getDescription());
        detail.setPrice(request.getPrice());

        return detail;
    }
}
