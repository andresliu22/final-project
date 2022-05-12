package com.example.compositeserver.Domain.EmployeeService;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class EmployeeAddrContact {
  private Integer employeeId;
  private Integer userId;
  private String firstName;
  private String lastName;
  private String email;
  private Integer cellPhone;
  private Address address;
  private Set<EmergencyContact> emergencyContacts;
}
