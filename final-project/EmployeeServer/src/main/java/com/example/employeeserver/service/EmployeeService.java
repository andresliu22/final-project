package com.example.employeeserver.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmergencyContactRepository ecRepository;

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        if (!employeeList.isEmpty()){
            return employeeList;
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
    public Integer updateEmployeeById(Employee employee, Integer id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee updatedEmployee = employeeOptional.get();
            Optional<Address> addressOptional = addressRepository.findById(updatedEmployee.getAddress().getAddressId());

            Optional<List<EmergencyContact>> ecListOptional = ecRepository.findAllByEmployee(updatedEmployee);
            List<EmergencyContact> ecList = ecListOptional.get();
            for (int i = 0; i < ecList.size(); i++){
                EmergencyContact ectemp = ecList.get(i);
                ecRepository.delete(ectemp);
            }
            updatedEmployee.setCellPhone(employee.getCellPhone());
            updatedEmployee.setEmail(employee.getEmail());
            Address updatedAddress = addressOptional.get();
            updatedAddress.setAddressLine1(employee.getAddress().getAddressLine1());
            updatedAddress.setAddressLine2(employee.getAddress().getAddressLine2());
            updatedAddress.setCity(employee.getAddress().getCity());
            updatedAddress.setState(employee.getAddress().getState());
            updatedAddress.setZipcode(employee.getAddress().getZipcode());
            Iterator<EmergencyContact> ecIterator = employee.getEmergencyContacts().iterator();
            while(ecIterator.hasNext()) {
                EmergencyContact updatedEc = ecIterator.next();
                updatedEc.setEmployee(updatedEmployee);
                ecRepository.save(updatedEc);
            }

            employeeRepository.save(updatedEmployee);
            addressRepository.save(updatedAddress);
            return updatedEmployee.getEmployeeId();
        }

        throw new NoEmployeeFoundException(String.format("Employee with id [%s] not found", id));
    }

}
