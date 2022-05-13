package com.example.compositeserver.Domain.EmployeeService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Address {
  private Integer addressId;
  private Integer employeeId;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String zipcode;
  private String state;
}
