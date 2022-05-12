package com.example.compositeserver.Domain.EmployeeService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmergencyContact {
  private Integer emergencyContactId;
  private Integer employeeId;
  private String firstName;
  private String lastName;
  private Integer cellPhone;
}
