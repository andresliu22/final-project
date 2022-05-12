package com.example.employeeserver.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class EmergencyContactDomain {
  private Integer emergencyContactId;
  private Integer employeeId;
  private String firstName;
  private String lastName;
  private Integer cellPhone;
}
