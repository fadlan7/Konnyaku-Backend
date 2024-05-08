package com.enigma.konyaku.controller;

import com.enigma.konyaku.dto.response.CommonResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<CommonResponse<?>> responseStatusExceptionHandler(ResponseStatusException exception) {
        // <?> == Object class
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(exception.getStatusCode().value())
                .message(exception.getReason())
                .build();

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<CommonResponse<?>> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        CommonResponse.CommonResponseBuilder<Object> builder = CommonResponse.builder();

        HttpStatus httpStatus;

        if (e.getMessage().contains("foreign key constraint")) {
            builder.statusCode(HttpStatus.BAD_REQUEST.value());
            builder.message("tidak dapat menghapus data karena ada referensi dari tabel lain");
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().contains("unique constraint") || e.getMessage().contains("duplicate key")) {
            builder.statusCode(HttpStatus.CONFLICT.value());
            builder.message("Data already exist");
            httpStatus = HttpStatus.CONFLICT;
        } else {
            builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            builder.message("Internal Server Error");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(httpStatus).body(builder.build());
    }
}
