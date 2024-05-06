package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.dto.request.SearchRequest;
import com.enigma.konyaku.dto.request.WishListRequest;
import com.enigma.konyaku.entity.Product;
import com.enigma.konyaku.entity.User;
import com.enigma.konyaku.entity.WishList;
import com.enigma.konyaku.repository.WishListRepository;
import com.enigma.konyaku.service.ProductService;
import com.enigma.konyaku.service.UserService;
import com.enigma.konyaku.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository repository;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public WishList create(WishListRequest request) {
        User user = userService.findByAccountId(request.getAccountId());
        Product product = productService.getProductById(request.getProductId());

        return repository.saveAndFlush(
                WishList.builder()
                        .user(user)
                        .product(product)
                        .build()
        );
    }

    @Override
    public Page<WishList> getAll(SearchRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()),request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        return repository.findAll(pageable);
    }
}
