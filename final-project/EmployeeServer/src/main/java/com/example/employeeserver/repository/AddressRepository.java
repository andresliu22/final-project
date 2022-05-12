package com.example.employeeserver.repository;

import com.example.employeeserver.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}