package com.example.compositeserver.service;

import com.example.compositeserver.CompositeServerApplication;
import com.example.compositeserver.Domain.EmployeeService.Address;
import com.example.compositeserver.Domain.EmployeeService.EmergencyContact;
import com.example.compositeserver.Domain.EmployeeService.Employee;
import com.example.compositeserver.Domain.EmployeeService.EmployeeAddrContact;
import com.example.compositeserver.Service.CompositeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CompositeServerApplication.class)
public class CompositeServiceTest {

    @Autowired
    private CompositeService compositeService;

    private static List<EmployeeAddrContact> list;
    private EmployeeAddrContact employee1;
    private Address address1;
    private String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmRyZXMiLCJyb2xlcyI6IlJPTEVfVVNFUiIsImlhdCI6MTY1MjQzMTYxMiwiZXhwIjoxNjUzNDMwNjEyfQ.CAVweBhYRDFYpeq1-JSM2RFK4Po99IOJMf8d8FtrwEc";
    static {
        list = new ArrayList<>();
        list.add(EmployeeAddrContact.builder().employeeId(1).userId(1).firstName("andres").lastName("liu").email("curiosity@gmail.com").cellPhone(11111).build());
        list.add(EmployeeAddrContact.builder().employeeId(2).userId(2).firstName("cindy").lastName("ko").email("curiosity@gmail.com").cellPhone(11111).build());
        list.add(EmployeeAddrContact.builder().employeeId(3).userId(3).firstName("syd").lastName("peng").email("syd@gmail.com").cellPhone(432434).build());
    }

    @BeforeEach
    public void setup() {
        address1 = Address.builder().addressId(1).addressLine1("123").addressLine2("456").city("SFO").state("CA").zipcode("12345").build();
        EmergencyContact ec1 = EmergencyContact.builder().emergencyContactId(8).employeeId(1).firstName("Yo").lastName("Heyhey").cellPhone(99999).build();
        EmergencyContact ec2 = EmergencyContact.builder().emergencyContactId(9).employeeId(1).firstName("Yo").lastName("Heyhey").cellPhone(99999).build();
        Set<EmergencyContact> ecdSet = new HashSet<EmergencyContact>();
        ecdSet.add(ec1);
        ecdSet.add(ec2);
        employee1 = EmployeeAddrContact.builder().employeeId(1).userId(1).firstName("andres").lastName("liu").email("andresliu@gmail.com").cellPhone(123123).address(address1).emergencyContacts(ecdSet).build();
    }

    @Test
    public void getAllEmployeeTest(){
        assertAll(
                () -> assertEquals(list.stream().filter((e)->e.getFirstName().equals("andres")).collect(Collectors.toList()).get(0).getFirstName(), compositeService.getAllEmployees(token).get(0).getFirstName()),
                () -> assertEquals(list.stream().filter((e)->e.getFirstName().equals("cindy")).collect(Collectors.toList()).get(0).getFirstName(), compositeService.getAllEmployees(token).get(1).getFirstName()),
                () -> assertEquals(list.stream().filter((e)->e.getFirstName().equals("syd")).collect(Collectors.toList()).get(0).getFirstName(), compositeService.getAllEmployees(token).get(2).getFirstName())
        );
    }

    @Test
    public void updateEmployeeByIdTest() {
        assertAll(
                () -> assertEquals(employee1.getEmail(), compositeService.updateEmployeeById(token, employee1, employee1.getUserId()).getEmail()),
                () -> assertEquals(employee1.getCellPhone(), compositeService.updateEmployeeById(token, employee1, employee1.getUserId()).getCellPhone()),
                () -> assertEquals(employee1.getAddress().getAddressLine1(), compositeService.updateEmployeeById(token, employee1, employee1.getUserId()).getAddress().getAddressLine1())
        );

    }


}