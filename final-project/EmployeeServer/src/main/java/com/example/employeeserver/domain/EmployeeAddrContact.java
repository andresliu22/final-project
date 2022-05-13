package com.example.employeeserver.domain;

import com.example.employeeserver.entity.Address;
import com.example.employeeserver.entity.EmergencyContact;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Data
@Builder
@Getter
@Setter
public class EmployeeAddrContact {
  private Integer employeeId;
  private Integer userId;
  private String firstName;
  private String lastName;
  private String email;
  private Integer cellPhone;
  private AddressDomain address;
  private Set<EmergencyContactDomain> emergencyContacts;
}
