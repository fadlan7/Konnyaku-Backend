package com.enigma.konyaku.entity;

import com.enigma.konyaku.constant.ProductAvailability;
import com.enigma.konyaku.constant.TableConstant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableConstant.PRODUCT)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "weight")
    private Integer weight;
    @OneToOne
    @JoinColumn(name = "thumbnail_image_id")
    private Image image;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    private Shop shop;
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductDetail> details;
    @Column(name = "status")
    private ProductAvailability status;
}
