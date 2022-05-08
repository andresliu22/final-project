package com.example.employeeserver.repository;

import com.example.employeeserver.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
//    Optional<Employee> findEmployeeByName(String name);
    Optional<Employee> updateEmployeeById(Employee employee, Integer employeeId);
}
