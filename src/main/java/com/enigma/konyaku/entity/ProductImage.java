package com.enigma.konyaku.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    @JsonBackReference
    private ProductDetail productDetail;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
