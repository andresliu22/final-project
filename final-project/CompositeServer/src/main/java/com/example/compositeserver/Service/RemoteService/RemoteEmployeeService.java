package com.example.compositeserver.Service.RemoteService;


import com.example.compositeserver.Domain.EmployeeService.Employee;
import com.example.compositeserver.Domain.EmployeeService.EmployeeAddrContact;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("employee-service")
public interface RemoteEmployeeService {

    @GetMapping("employee-service/getAllEmployees")
    ResponseEntity<List<EmployeeAddrContact>> getAllEmployees(@RequestHeader("Authorization") String token);

    @PostMapping("employee-service/update-employee/{employeeId}")
    ResponseEntity<Integer> updateEmployeeById(@RequestHeader("Authorization") String token, @RequestBody EmployeeAddrContact employee, @PathVariable Integer employeeId);

//    @GetMapping("/employee-service/employee-id/{employeeId}")
//    ResponseEntity<EmployeeAddrContact> findEmployeeById(@RequestHeader("Authorization") String token, @PathVariable Integer employeeId);

//    @PostMapping("xxxxx")
//    ResponseEntity<List<User>> addUser(@RequestBody User user);
//
//    @GetMapping("xxxxxxxx/{userId}")
//    ResponseEntity<List<User>> getByID(@PathVariable Integer id, @RequestHeader("Authorization") String token);

}
