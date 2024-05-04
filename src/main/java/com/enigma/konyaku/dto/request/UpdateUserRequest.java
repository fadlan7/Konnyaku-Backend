package com.enigma.konyaku.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    private String id;
    private String name;
    private String mobilePhoneNo;
}
