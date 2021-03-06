package com.example.employeeserver.service;

import com.example.employeeserver.domain.AddressDomain;
import com.example.employeeserver.domain.EmergencyContactDomain;
import com.example.employeeserver.domain.EmployeeAddrContact;
import com.example.employeeserver.domain.EmployeeDomain;
import com.example.employeeserver.entity.Address;
import com.example.employeeserver.entity.EmergencyContact;
import com.example.employeeserver.entity.Employee;
import com.example.employeeserver.exception.NoEmployeeFoundException;
import com.example.employeeserver.repository.AddressRepository;
import com.example.employeeserver.repository.EmergencyContactRepository;
import com.example.employeeserver.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmergencyContactRepository ecRepository;

    @Transactional(readOnly = true)
    public List<EmployeeAddrContact> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        if (!employeeList.isEmpty()) {
            List<EmployeeAddrContact> employees = employeeList.stream().map(
                e -> {
                    AddressDomain addrDomain = AddressDomain.builder()
                        .employeeId(e.getEmployeeId())
                        .addressId(e.getAddress().getAddressId())
                        .addressLine1(e.getAddress().getAddressLine1())
                        .addressLine2(e.getAddress().getAddressLine2())
                        .state(e.getAddress().getState())
                        .zipcode(e.getAddress().getZipcode())
                        .city(e.getAddress().getCity()).build();
                    Set<EmergencyContactDomain> ecdSet = new HashSet<EmergencyContactDomain>();
                    Iterator<EmergencyContact> ecSetIt= e.getEmergencyContacts().iterator();
                    while(ecSetIt.hasNext()) {
                        EmergencyContact updatedEc = ecSetIt.next();
                        EmergencyContactDomain ecd = EmergencyContactDomain.builder()
                            .employeeId(updatedEc.getEmployee().getEmployeeId())
                            .emergencyContactId(updatedEc.getEmergencyContactId())
                            .cellPhone(updatedEc.getCellPhone())
                            .firstName(updatedEc.getFirstName())
                            .lastName(updatedEc.getLastName()).build();
                        ecdSet.add(ecd);
                    }
                    EmployeeAddrContact employee = EmployeeAddrContact.builder()
                        .employeeId(e.getEmployeeId())
                        .userId(e.getEmployeeId())
                        .firstName(e.getFirstName())
                        .lastName(e.getLastName())
                        .email(e.getEmail())
                        .cellPhone(e.getCellPhone())
                        .address(addrDomain)
                        .emergencyContacts(ecdSet)
                        .build();

                    return employee;
                }).collect(Collectors.toList());
            return employees;
        }
        throw new NoEmployeeFoundException(String.format("No Employee found"));
    }
    @Transactional(readOnly = true)
    public EmployeeDomain findEmployeeById(Integer id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            return EmployeeDomain
                    .builder()
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .cellPhone(employee.getCellPhone())
                    .build();
        }

        throw new NoEmployeeFoundException(String.format("Employee with id [%s] not found", id));
    }

    @Transactional()
    public EmployeeAddrContact updateEmployeeById(Employee employee, Integer id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee updatedEmployee = employeeOptional.get();
            EmployeeAddrContact empAddrCon = EmployeeAddrContact.builder()
                    .employeeId(updatedEmployee.getEmployeeId())
                    .userId(updatedEmployee.getUserId())
                    .firstName(updatedEmployee.getFirstName())
                    .lastName(updatedEmployee.getLastName())
                    .email(updatedEmployee.getEmail())
                    .cellPhone(updatedEmployee.getCellPhone())
                    .build();
            Optional<Address> addressOptional = addressRepository.findById(updatedEmployee.getAddress().getAddressId());
            if (addressOptional.isPresent()) {
                Address updatedAddress = addressOptional.get();
                updatedAddress.setAddressLine1(employee.getAddress().getAddressLine1());
                updatedAddress.setAddressLine2(employee.getAddress().getAddressLine2());
                updatedAddress.setCity(employee.getAddress().getCity());
                updatedAddress.setState(employee.getAddress().getState());
                updatedAddress.setZipcode(employee.getAddress().getZipcode());
                addressRepository.save(updatedAddress);

                AddressDomain addressDomain = AddressDomain.builder()
                        .addressId(updatedAddress.getAddressId())
                        .employeeId(updatedAddress.getEmployee().getEmployeeId())
                        .addressLine1(updatedAddress.getAddressLine1())
                        .addressLine2(updatedAddress.getAddressLine2())
                        .state(updatedAddress.getState())
                        .city(updatedAddress.getCity())
                        .zipcode(updatedAddress.getZipcode())
                        .build();
                empAddrCon.setAddress(addressDomain);
            }

            Optional<List<EmergencyContact>> ecListOptional = ecRepository.findAllByEmployee(updatedEmployee);

            if (ecListOptional.isPresent()) {
                if(employee.getEmergencyContacts() != null ) {
                    List<EmergencyContact> ecList = ecListOptional.get();
                    for (int i = 0; i < ecList.size(); i++){
                        EmergencyContact ectemp = ecList.get(i);
                        ecRepository.delete(ectemp);
                    }
                    Iterator<EmergencyContact> ecIterator = employee.getEmergencyContacts().iterator();
                    Set<EmergencyContactDomain> ecdSet = new HashSet<EmergencyContactDomain>();
                    while(ecIterator.hasNext()) {
                        EmergencyContact updatedEc = ecIterator.next();
                        updatedEc.setEmployee(updatedEmployee);
                        ecRepository.save(updatedEc);

                        EmergencyContactDomain ecd = EmergencyContactDomain.builder()
                                .employeeId(updatedEc.getEmployee().getEmployeeId())
                                .emergencyContactId(updatedEc.getEmergencyContactId())
                                .cellPhone(updatedEc.getCellPhone())
                                .firstName(updatedEc.getFirstName())
                                .lastName(updatedEc.getLastName()).build();
                        ecdSet.add(ecd);
                    }
                    empAddrCon.setEmergencyContacts(ecdSet);
                }
            }

            updatedEmployee.setCellPhone(employee.getCellPhone());
            updatedEmployee.setEmail(employee.getEmail());

            employeeRepository.save(updatedEmployee);
            return empAddrCon;
        }

        throw new NoEmployeeFoundException(String.format("Employee with id [%s] not found", id));
    }


}
