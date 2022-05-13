package com.example.employeeserver.domain;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class AddressDomain {
  private Integer addressId;
  private Integer employeeId;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String zipcode;
  private String state;


}
