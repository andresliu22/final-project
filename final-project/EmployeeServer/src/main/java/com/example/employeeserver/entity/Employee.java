package com.example.employeeserver.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "cell_phone")
    private Integer cellPhone;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee",cascade=CascadeType.ALL)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.MERGE)
    private Set<EmergencyContact> emergencyContacts = new HashSet<>();
}
