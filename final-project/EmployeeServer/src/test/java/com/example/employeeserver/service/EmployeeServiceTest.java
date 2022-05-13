package com.example.employeeserver.service;

import com.example.employeeserver.EmployeeServerApplication;
import com.example.employeeserver.controller.EmployeeController;
import com.example.employeeserver.domain.AddressDomain;
import com.example.employeeserver.domain.EmployeeAddrContact;
import com.example.employeeserver.entity.Address;
import com.example.employeeserver.entity.Employee;
import com.example.employeeserver.exception.NoEmployeeFoundException;
import com.example.employeeserver.repository.EmployeeRepository;
import com.example.employeeserver.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
@SpringBootTest(classes = EmployeeServerApplication.class)
public class EmployeeServiceTest {

  @Autowired
  private EmployeeService employeeService;

  private static List<EmployeeAddrContact> list;
  private Employee employee1;
  private Address address1;

  static {
    list = new ArrayList<>();
    list.add(EmployeeAddrContact.builder().employeeId(1).userId(1).firstName("andres").lastName("liu").email("curiosity@gmail.com").cellPhone(11111).build());
    list.add(EmployeeAddrContact.builder().employeeId(2).userId(2).firstName("cindy").lastName("ko").email("curiosity@gmail.com").cellPhone(11111).build());
    list.add(EmployeeAddrContact.builder().employeeId(3).userId(3).firstName("syd").lastName("peng").email("syd@gmail.com").cellPhone(432434).build());

  }

  @BeforeEach
  public void setup() throws NoEmployeeFoundException {
    address1 = Address.builder().addressId(1).addressLine1("123").addressLine2("456").city("SFO").state("CA").zipcode("12345").build();
    employee1 = Employee.builder().employeeId(1).userId(1).firstName("andres").lastName("liu").email("andresliu@gmail.com").cellPhone(123123).address(address1).build();
  }

  @Test
  public void getAllEmployeesTest(){
    assertAll(
            () -> assertEquals(list.stream().filter((e)->e.getFirstName().equals("andres")).collect(Collectors.toList()).get(0).getFirstName(), employeeService.getAllEmployees().get(0).getFirstName()),
            () -> assertEquals(list.stream().filter((e)->e.getFirstName().equals("cindy")).collect(Collectors.toList()).get(0).getFirstName(), employeeService.getAllEmployees().get(1).getFirstName()),
            () -> assertEquals(list.stream().filter((e)->e.getFirstName().equals("syd")).collect(Collectors.toList()).get(0).getFirstName(), employeeService.getAllEmployees().get(2).getFirstName())
    );
  }

  @Test
  public void updateEmployeeByIdTest() {
    assertAll(
            () -> assertEquals(employee1.getEmail(), employeeService.updateEmployeeById(employee1, employee1.getUserId()).getEmail()),
            () -> assertEquals(employee1.getCellPhone(), employeeService.updateEmployeeById(employee1, employee1.getUserId()).getCellPhone()),
            () -> assertEquals(employee1.getAddress().getAddressLine1(), employeeService.updateEmployeeById(employee1, employee1.getUserId()).getAddress().getAddressLine1())
    );

  }
}