package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
}
