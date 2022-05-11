package com.example.compositeserver.Service.RemoteService;


import com.example.compositeserver.Domain.EmployeeService.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("employee-service")
public interface RemoteEmployeeService {

    @GetMapping("employee-service/getAllEmployees")
    ResponseEntity<List<Employee>> getAllEmployees(@RequestHeader("Authorization") String token);

//
//    @PostMapping("xxxxx")
//    ResponseEntity<List<User>> addUser(@RequestBody User user);
//
//    @GetMapping("xxxxxxxx/{userId}")
//    ResponseEntity<List<User>> getByID(@PathVariable Integer id, @RequestHeader("Authorization") String token);

}
