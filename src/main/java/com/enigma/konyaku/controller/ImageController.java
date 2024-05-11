package com.enigma.konyaku.controller;

import com.enigma.konyaku.constant.ApiUrl;
import com.enigma.konyaku.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(ApiUrl.API_IMAGE_DOWNLOAD)
public class ImageController {
    private final ImageService service;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable String id) {
        Resource resource = service.getById(id);
        String headerValue = String.format("attachment; filename=%s", resource.getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
