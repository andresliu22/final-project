package com.example.employeeserver.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class EmployeeDomain {
    String firstName;
    String lastName;
    String email;
    Integer cellPhone;
}
