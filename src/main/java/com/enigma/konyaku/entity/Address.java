package com.enigma.konyaku.entity;

import com.enigma.konyaku.constant.TableConstant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableConstant.ADDRESS)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "street")
    private String street;
    @Column(name = "province_id")
    private String provinceId;
    @Column(name = "province_name")
    private String provinceName;
    @Column(name = "city_id")
    private String cityId;
    @Column(name = "city_name")
    private String cityName;
}
