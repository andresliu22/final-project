package com.example.authserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_role")
    private String userRole;

//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.MERGE)
//    private User user;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.MERGE)
//    private Set<Address> addresses = new HashSet<>();
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.MERGE)
//    private Set<EmergencyContact> emergencyContacts = new HashSet<>();
}
