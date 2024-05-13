package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.constant.ResponseMessage;
import com.enigma.konyaku.dto.request.WishListRequest;
import com.enigma.konyaku.dto.response.ImageResponse;
import com.enigma.konyaku.dto.response.ProductDetailResponse;
import com.enigma.konyaku.dto.response.ProductResponse;
import com.enigma.konyaku.dto.response.WishListResponse;
import com.enigma.konyaku.entity.Product;
import com.enigma.konyaku.entity.ProductDetail;
import com.enigma.konyaku.entity.User;
import com.enigma.konyaku.entity.WishList;
import com.enigma.konyaku.repository.WishListRepository;
import com.enigma.konyaku.service.ProductService;
import com.enigma.konyaku.service.UserService;
import com.enigma.konyaku.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private static final Logger log = LoggerFactory.getLogger(WishListServiceImpl.class);
    private final WishListRepository repository;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public WishList create(WishListRequest request) {
        var check = checkExisting(request.getAccountId(), request.getProductId());
        System.out.println(check);
        if (check) {
            throw new RuntimeException(ResponseMessage.EXISTING_WISHLIST);
        } else {
            User user = userService.findByAccountId(request.getAccountId());
            Product product = productService.getProductById(request.getProductId());
            return repository.saveAndFlush(
                    WishList.builder()
                            .user(user)
                            .product(product)
                            .build()
            );
        }

    }

    @Override
    public List<WishListResponse> getAll(String id) {
        User user = userService.findByAccountId(id);
        List<WishList> wishLists = repository.findWishListByUserId(user.getId());

        return wishLists.stream().map(wishList -> {
            int priceAmount = wishList.getProduct().getDetails().stream().mapToInt(ProductDetail::getPrice).reduce(0, Integer::sum);

            ProductResponse productResponse = ProductResponse.builder()
                    .id(wishList.getProduct().getId())
                    .name(wishList.getProduct().getName())
                    .priceAmount(priceAmount)
                    .description(wishList.getProduct().getDescription())
                    .thumbnail(
                            ImageResponse.builder()
                                    .name(wishList.getProduct().getImage().getName())
                                    .url(ApiUrl.API_IMAGE_DOWNLOAD + wishList.getProduct().getImage().getId())
                                    .build()
                    )
                    .weight(wishList.getProduct().getWeight())
                    .status(wishList.getProduct().getStatus())
                    .details(wishList.getProduct().getDetails().stream().map(
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

            return WishListResponse.builder()
                    .id(wishList.getId())
                    .product(productResponse)
                    .build();
        }).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean checkExisting(String userAccId, String productId) {
        User user = userService.findByAccountId(userAccId);

        return repository.findingNemo(user.getId(), productId);
    }
}
