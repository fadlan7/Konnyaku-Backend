package com.enigma.konyaku.entity;

import com.enigma.konyaku.constant.TableConstant;
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
@Table(name = TableConstant.USER)
public class User {
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
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<AccountImage> identificationImages;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<WishList> wishList;
    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true)
    private UserAccount userAccount;
}