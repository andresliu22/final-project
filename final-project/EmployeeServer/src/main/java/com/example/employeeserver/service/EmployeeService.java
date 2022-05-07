package com.example.employeeserver.service;

import com.example.employeeserver.domain.EmployeeDomain;
import com.example.employeeserver.entity.Employee;
import com.example.employeeserver.exception.NoEmployeeFoundException;
import com.example.employeeserver.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

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

//    @Transactional(readOnly = true)
//    public List<EmployeeDomain> findSync(List<String> names){
//        List<EmployeeDomain> res = names.stream().map(name -> {
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e){
//                e.printStackTrace();
//            }
//
//            return employeeRepository.findEmployeeByName(name);
//        }).filter(Optional::isPresent).map(po -> {
//            Employee employee = po.get();
//            return EmployeeDomain.builder().firstName(employee.getFirstName())
//                    .lastName(employee.getLastName())
//                    .email(employee.getEmail())
//                    .cellPhone(employee.getCellPhone())
//                    .build();
//        }).collect(Collectors.toList());
//
//        return res;
//    }
}
