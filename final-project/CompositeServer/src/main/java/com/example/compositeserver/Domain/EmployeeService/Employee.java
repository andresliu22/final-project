package com.example.compositeserver.Domain.EmployeeService;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Employee {
  private Integer employeeId;
  private Integer userId;
  private String firstName;
  private String lastName;
  private String email;
  private Integer cellPhone;

}
