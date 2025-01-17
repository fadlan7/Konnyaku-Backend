package com.enigma.konyaku.entity;

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
@Table(name = TableConstant.PRODUCT_DETAIL)
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
    @Column(name = "price")
    private Integer price;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
