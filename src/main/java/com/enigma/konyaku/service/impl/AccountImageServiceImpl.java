package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.entity.AccountImage;
import com.enigma.konyaku.repository.AccountImageRepository;
import com.enigma.konyaku.service.AccountImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountImageServiceImpl implements AccountImageService {
    private  final AccountImageRepository repository;

    @Override
    public List<AccountImage> create(List<AccountImage> accountImages) {
        return repository.saveAllAndFlush(accountImages);
    }
}
