package com.example.employeeserver.service;


import com.example.employeeserver.EmployeeServerApplication;
import com.example.employeeserver.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EmployeeServerApplication.class)
public class EmployeeServiceTest {
  @Autowired
  private EmployeeRepository employeeRepository;


}
