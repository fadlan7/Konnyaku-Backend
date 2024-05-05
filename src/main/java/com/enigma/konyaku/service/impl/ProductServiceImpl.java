package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.constant.ProductAvailability;
import com.enigma.konyaku.dto.request.NewProductRequest;
import com.enigma.konyaku.dto.request.SearchProductRequest;
import com.enigma.konyaku.dto.request.UpdateProductRequest;
import com.enigma.konyaku.dto.response.ImageResponse;
import com.enigma.konyaku.dto.response.ProductDetailResponse;
import com.enigma.konyaku.dto.response.ProductResponse;
import com.enigma.konyaku.entity.Image;
import com.enigma.konyaku.entity.Product;
import com.enigma.konyaku.entity.ProductDetail;
import com.enigma.konyaku.repository.ProductRepository;
import com.enigma.konyaku.service.ProductService;
import com.enigma.konyaku.service.ImageService;
import com.enigma.konyaku.service.ProductDetailService;
import com.enigma.konyaku.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductDetailService detailService;
    private final ImageService imageService;
    private final ShopService shopService;

    @Override
    public ProductResponse create(NewProductRequest request) {
        Product product = repository.saveAndFlush(
                Product.builder()
                        .name(request.getName())
                        .shop(shopService.getShopById(request.getShopId()))
                        .description(request.getDescription())
                        .weight(request.getWeight())
                        .status(ProductAvailability.available)
                        .build()
        );

        List<ProductDetail> details = detailService.create(request.getDetails().stream().map(
                (detailRequest) -> {
                    Image image = imageService.create(detailRequest.getImage());

                   return ProductDetail.builder()
                           .product(product)
                           .price(detailRequest.getPrice())
                           .name(detailRequest.getName())
                           .description(detailRequest.getDescription())
                           .image(image)
                           .build();
                }
        ).toList());
        product.setDetails(details);

        List<ProductDetailResponse> detailResponses = details.stream().map(
                detail -> {
                    return ProductDetailResponse.builder()
                            .id(detail.getId())
                            .name(detail.getName())
                            .description(detail.getDescription())
                            .price(detail.getPrice())
                            .image(
                                    ImageResponse.builder()
                                            .name(detail.getImage().getName())
                                            .url(ApiUrl.API_MENU_IMAGE_DOWNLOAD + detail.getImage().getId())
                                            .build()
                            )
                            .build();
                }
        ).toList();

        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .weight(product.getWeight())
                .status(product.getStatus())
                .details(detailResponses)
                .build();
    }

    @Override
    public Page<ProductResponse> getAll(SearchProductRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()),request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<Product> productPage = repository.findAll(pageable);

        Integer minPrice = 0;
        Integer maxPrice = 2000000000;
        String name = "%%";

        if (request.getMinPrice() != null) {
            minPrice = request.getMinPrice();
        }

        if (request.getMaxPrice() != null) {
            maxPrice = request.getMaxPrice();
        }

        if (request.getName() != null) {
            name = "%" + request.getName().toLowerCase() + "%";
        }

        if (repository.findAllFiltered(minPrice, maxPrice, name).isPresent()){
            List<Product> products = repository.findAllFiltered(minPrice, maxPrice, name).get();
            productPage = new PageImpl<>(products, pageable, products.size());
        }

        return productPage.map(
                product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .weight(product.getWeight())
                        .status(product.getStatus())
                        .details(product.getDetails().stream().map(
                                detail -> ProductDetailResponse.builder()
                                        .id(detail.getId())
                                        .name(detail.getName())
                                        .description(detail.getDescription())
                                        .price(detail.getPrice())
                                        .image(
                                                ImageResponse.builder()
                                                        .name(detail.getImage().getName())
                                                        .url(ApiUrl.API_MENU_IMAGE_DOWNLOAD + detail.getImage().getId())
                                                        .build()
                                        )
                                        .build()
                        ).toList())
                        .build()
        );
    }

    @Override
    public Product getProductById(String id) {
        Optional<Product> product = repository.findById(id);
        if (product.isEmpty()) throw new RuntimeException("Product Not Found");
        return product.get();
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = getProductById(id);
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .weight(product.getWeight())
                .status(product.getStatus())
                .details(product.getDetails().stream().map(
                        detail -> ProductDetailResponse.builder()
                                .id(detail.getId())
                                .name(detail.getName())
                                .description(detail.getDescription())
                                .price(detail.getPrice())
                                .image(
                                        ImageResponse.builder()
                                                .name(detail.getImage().getName())
                                                .name(ApiUrl.API_MENU_IMAGE_DOWNLOAD + detail.getImage().getId())
                                                .build()
                                )
                                .build()
                ).toList())
                .build();
    }

    @Override
    public ProductResponse update(UpdateProductRequest request) {
        Product product = getProductById(request.getId());

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());

        repository.saveAndFlush(product);

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .weight(product.getWeight())
                .status(product.getStatus())
                .details(product.getDetails().stream().map(
                        detail -> ProductDetailResponse.builder()
                                .id(detail.getId())
                                .name(detail.getName())
                                .description(detail.getDescription())
                                .price(detail.getPrice())
                                .image(
                                        ImageResponse.builder()
                                                .name(detail.getImage().getName())
                                                .url(ApiUrl.API_MENU_IMAGE_DOWNLOAD + detail.getImage().getId())
                                                .build()
                                )
                                .build()
                ).toList())
                .build();
    }

    @Override
    public void delete(String id) {
    }
}