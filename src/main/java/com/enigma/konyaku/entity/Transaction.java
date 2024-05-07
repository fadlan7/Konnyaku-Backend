package com.enigma.konyaku.entity;

import com.enigma.konyaku.constant.TableConstant;
import com.enigma.konyaku.constant.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableConstant.TRANSACTION)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "trans_date", updatable = false)
    private Date transDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "rent_start")
    private Date rentStart;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "rent_end")
    private Date rentEnd;
    @Column(name = "status")
    private TransactionStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;
}
