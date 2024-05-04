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
@Table(name = TableConstant.SHOP)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile_phone_no")
    private String mobilePhoneNo;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @Column(name = "activity")
    private Boolean activity;
    @Column(name = "availability")
    private Boolean availability;
    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true)
    private UserAccount userAccount;
}
