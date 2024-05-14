package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.constant.ProductAvailability;
import com.enigma.konyaku.constant.ResponseMessage;
import com.enigma.konyaku.dto.request.*;
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
import com.enigma.konyaku.specification.ProductSpecification;
import com.enigma.konyaku.specification.RandomProductSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository repository;
    private final ProductDetailService detailService;
    private final ImageService imageService;
    private final ShopService shopService;

    @Override
    public ProductResponse create(NewProductRequest request) {
        Product product = repository.saveAndFlush(
                Product.builder()
                        .name(request.getName())
                        .image(
                                imageService.create(request.getThumbnail())
                        )
                        .shop(shopService.getShopById(request.getShopId()))
                        .description(request.getDescription())
                        .weight(request.getWeight())
                        .status(ProductAvailability.AVAILABLE)
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
                                            .url(ApiUrl.API_IMAGE_DOWNLOAD + detail.getImage().getId())
                                            .build()
                            )
                            .build();
                }
        ).toList();

        int priceAmount = details.stream().mapToInt(ProductDetail::getPrice).reduce(0, Integer::sum);

        return ProductResponse.builder()
                .name(product.getName())
                .priceAmount(priceAmount)
                .description(product.getDescription())
                .thumbnail(
                        ImageResponse.builder()
                                .name(product.getImage().getName())
                                .url(ApiUrl.API_IMAGE_DOWNLOAD + product.getImage().getId())
                                .build()
                )
                .weight(product.getWeight())
                .status(product.getStatus())
                .details(detailResponses)
                .build();
    }

    @Override
    public Page<ProductResponse> getAll(SearchProductRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<Product> productPage = repository.findAll(pageable);

        Integer minPrice = 0;
        Integer maxPrice = 2147483647;

        if (request.getMinPrice() != null) {
            minPrice = request.getMinPrice();
        }

        if (request.getMaxPrice() != null) {
            maxPrice = request.getMaxPrice();
        }


        if (repository.findAllFiltered(minPrice, maxPrice).isPresent()) {
            List<Product> products = repository.findAllFiltered(minPrice, maxPrice).get();
            productPage = new PageImpl<>(products, pageable, products.size());
        }

        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> {
                    List<ProductDetailResponse> detailResponse = product.getDetails().stream()
                            .map(productDetail -> {
                                ImageResponse imageResponse = ImageResponse.builder()
                                        .url(ApiUrl.API_IMAGE_DOWNLOAD + productDetail.getImage().getId())
                                        .name(productDetail.getImage().getName())
                                        .build();

                                return ProductDetailResponse.builder()
                                        .id(productDetail.getId())
                                        .name(productDetail.getName())
                                        .description(productDetail.getDescription())
                                        .price(productDetail.getPrice())
                                        .image(imageResponse)
                                        .build();
                            }).toList();

                    ImageResponse imageResponse = ImageResponse.builder()
                            .url(ApiUrl.API_IMAGE_DOWNLOAD + product.getImage().getId())
                            .name(product.getImage().getName())
                            .build();

                    int priceAmount = detailResponse.stream().mapToInt(ProductDetailResponse::getPrice).reduce(0, Integer::sum);

                    return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .priceAmount(priceAmount)
                            .description(product.getDescription())
                            .thumbnail(imageResponse)
                            .weight(product.getWeight())
                            .status(product.getStatus())
                            .details(detailResponse)
                            .build();
                }).toList();
        return new PageImpl<>(productResponses, pageable, productPage.getTotalElements());
    }

    @Override
    public Product getProductById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = getProductById(id);
        return getProductResponse(product);
    }

    private ProductResponse getProductResponse(Product product) {
        int priceAmount = product.getDetails().stream().mapToInt(ProductDetail::getPrice).reduce(0, Integer::sum);
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .priceAmount(priceAmount)
                .description(product.getDescription())
                .thumbnail(
                        ImageResponse.builder()
                                .name(product.getImage().getName())
                                .url(ApiUrl.API_IMAGE_DOWNLOAD + product.getImage().getId())
                                .build()
                )
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
                                                .url(ApiUrl.API_IMAGE_DOWNLOAD + detail.getImage().getId())
                                                .build()
                                )
                                .build()
                ).toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(UpdateProductRequest request) {
        Product product = getProductById(request.getId());

        product.setName(request.getName());
        product.setDescription(request.getDescription());

        if(request.getThumbnail() != null){
            Image image = imageService.create(request.getThumbnail());
            product.setImage(image);
        }

        List<ProductDetail> productDetails = new ArrayList<>();
        for(UpdateProductDetailRequest detailRequest : request.getDetails()){
           ProductDetail productDetail =  detailService.update(detailRequest);
           productDetails.add(productDetail);
        }

        product.setDetails(productDetails);

        return getProductResponse(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Product product = getProductById(id);
        String thumbnailId = product.getImage().getId();
        List<String> productDetailsImageIds = new ArrayList<>();

        for (int i = 0; i < product.getDetails().size(); i++) {
            productDetailsImageIds.add(product.getDetails().get(i).getImage().getId());
        }
        product.setStatus(ProductAvailability.REMOVED);
        imageService.deleteFileImageById(thumbnailId);

        for (int i = 0; i < product.getDetails().size(); i++) {
            imageService.deleteFileImageById(productDetailsImageIds.get(i));
        }
    }

    @Override
    public Page<ProductResponse> getAllByShop(SearchProductByShopRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);

        Specification<Product> specification = ProductSpecification.getSpecification(request.getQ(), request.getShopId());

        Page<Product> products = repository.findAll(specification, pageable);

        List<ProductResponse> productResponses = products.getContent().stream()
                .map(product -> {
                    List<ProductDetailResponse> detailResponse = product.getDetails().stream()
                            .map(productDetail -> {
                                ImageResponse imageResponse = ImageResponse.builder()
                                        .url(ApiUrl.API_IMAGE_DOWNLOAD + productDetail.getImage().getId())
                                        .name(productDetail.getImage().getName())
                                        .build();

                                return ProductDetailResponse.builder()
                                        .id(productDetail.getId())
                                        .name(productDetail.getName())
                                        .description(productDetail.getDescription())
                                        .price(productDetail.getPrice())
                                        .image(imageResponse)
                                        .build();
                            }).toList();

                    ImageResponse imageResponse = ImageResponse.builder()
                            .url(ApiUrl.API_IMAGE_DOWNLOAD + product.getImage().getId())
                            .name(product.getImage().getName())
                            .build();

                    int priceAmount = detailResponse.stream().mapToInt(ProductDetailResponse::getPrice).reduce(0, Integer::sum);

                    return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .priceAmount(priceAmount)
                            .description(product.getDescription())
                            .thumbnail(imageResponse)
                            .weight(product.getWeight())
                            .status(product.getStatus())
                            .details(detailResponse)
                            .build();
                }).toList();
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Override
    public Page<ProductResponse> getAllRandom(SearchProductByShopRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Product> specification = RandomProductSpecification.getSpecification(request.getQ());


        Page<Product> productPages = repository.getAllByRandom(pageable);


        if (request.getQ() != null) {
            productPages = repository.findAll(specification, pageable);
        }

        List<ProductResponse> productResponses = productPages.getContent().stream()
                .map(product -> {
                    List<ProductDetailResponse> detailResponse = product.getDetails().stream()
                            .map(productDetail -> {
                                ImageResponse imageResponse = ImageResponse.builder()
                                        .url(ApiUrl.API_IMAGE_DOWNLOAD + productDetail.getImage().getId())
                                        .name(productDetail.getImage().getName())
                                        .build();

                                return ProductDetailResponse.builder()
                                        .id(productDetail.getId())
                                        .name(productDetail.getName())
                                        .description(productDetail.getDescription())
                                        .price(productDetail.getPrice())
                                        .image(imageResponse)
                                        .build();
                            }).toList();

                    ImageResponse imageResponse = ImageResponse.builder()
                            .url(ApiUrl.API_IMAGE_DOWNLOAD + product.getImage().getId())
                            .name(product.getImage().getName())
                            .build();

                    int priceAmount = detailResponse.stream().mapToInt(ProductDetailResponse::getPrice).reduce(0, Integer::sum);

                    return ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .priceAmount(priceAmount)
                            .description(product.getDescription())
                            .thumbnail(imageResponse)
                            .weight(product.getWeight())
                            .status(product.getStatus())
                            .details(detailResponse)
                            .build();
                }).toList();
        return new PageImpl<>(productResponses, pageable, productPages.getTotalElements());
    }
}
