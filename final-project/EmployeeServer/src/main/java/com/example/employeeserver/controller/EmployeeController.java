package com.example.employeeserver.controller;

import com.example.employeeserver.domain.EmployeeDomain;
import com.example.employeeserver.entity.Employee;
import com.example.employeeserver.exception.NoEmployeeFoundException;
import com.example.employeeserver.repository.EmployeeRepository;
import com.example.employeeserver.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("employee-service")
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) { this.employeeService = employeeService; }

    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<EmployeeDomain> getEmployeeById(@PathVariable Integer employeeId) {
        LOGGER.warn("/employee-id/" + employeeId);
        return ResponseEntity.ok().body(employeeService.findEmployeeById(employeeId));
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No Employee is found")
    @ExceptionHandler(NoEmployeeFoundException.class)
    public void noPetFound(HttpServletRequest req, NoEmployeeFoundException e) {
        LOGGER.warn("URI: "+req.getRequestURI()+", NoEmployeeFoundException: "+e.getMessage());
    }

    @PostMapping("/add-employee")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PostMapping("/update-employee/{employeeId}")
    public ResponseEntity<Integer> updateEmployee(@RequestBody Employee employee, @PathVariable Integer employeeId) {
        return ResponseEntity.ok().body(employeeService.updateEmployeeById(employee, employeeId));
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity getAllEmployees(){
        System.out.println(employeeService.getAllEmployees());
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}
