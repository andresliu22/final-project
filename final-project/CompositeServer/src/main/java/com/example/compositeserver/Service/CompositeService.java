package com.example.compositeserver.Service;


import com.example.compositeserver.Domain.EmployeeService.Employee;
import org.springframework.stereotype.Service;
import com.example.compositeserver.Service.RemoteService.RemoteEmployeeService;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompositeService {

    private RemoteEmployeeService remoteEmployeeService;

    private CompositeService(RemoteEmployeeService remoteEmployeeService){
        this.remoteEmployeeService = remoteEmployeeService;
    }

    public List<Employee> getAllEmployees(){
        List<Employee> EmployeeList = remoteEmployeeService.getAllEmployees().getBody();
        return EmployeeList;
    }
}
