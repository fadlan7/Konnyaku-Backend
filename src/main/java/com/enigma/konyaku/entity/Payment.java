package com.enigma.konyaku.entity;

import com.enigma.konyaku.constant.TableConstant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableConstant.PAYMENT)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "token")
    private String token;
    @Column(name = "redirect_url")
    private String redirectUrl;
    @Column(name = "transaction_status")
    private String status;
    @OneToOne(mappedBy = "payment")
    private Transaction transaction;
}
