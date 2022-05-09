package com.example.employeeserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
