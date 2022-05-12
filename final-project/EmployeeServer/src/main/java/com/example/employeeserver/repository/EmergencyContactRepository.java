package com.example.employeeserver.repository;

import com.example.employeeserver.entity.EmergencyContact;
import com.example.employeeserver.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {
    Optional<List<EmergencyContact>> findAllByEmployee(Employee e);

}