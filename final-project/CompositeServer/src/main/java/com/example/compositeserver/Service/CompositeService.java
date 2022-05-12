package com.example.compositeserver.Service;


import com.example.compositeserver.Domain.EmployeeService.Employee;
import com.example.compositeserver.Domain.EmployeeService.EmployeeAddrContact;
import com.example.compositeserver.Domain.TimeSheetService.SummaryDomain;
import com.example.compositeserver.Domain.TimeSheetService.TimeSheetDomain;
import com.example.compositeserver.Service.RemoteService.RemoteTimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.compositeserver.Service.RemoteService.RemoteEmployeeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompositeService {

    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    private RemoteTimeSheetService remoteTimeSheetService;

    private CompositeService(RemoteEmployeeService remoteEmployeeService){
        this.remoteEmployeeService = remoteEmployeeService;
    }

    public List<EmployeeAddrContact> getAllEmployees(String token){
        List<EmployeeAddrContact> EmployeeList = remoteEmployeeService.getAllEmployees(token).getBody();
        return EmployeeList;
    }

    public Integer updateEmployeeById(String token, EmployeeAddrContact employee, Integer id){
      Integer employeeId = remoteEmployeeService.updateEmployeeById(token, employee, id).getBody();
      return employeeId;
    }

//  public EmployeeAddrContact findEmployeeById(String token, Integer employeeId){
//    EmployeeAddrContact employee = remoteEmployeeService.findEmployeeById(token, employeeId).getBody();
//    return employee;
//  }

    public TimeSheetDomain createProduct(TimeSheetDomain tsd){
        TimeSheetDomain res=remoteTimeSheetService.createProduct(tsd).getBody();
        return res;
    }



    public void deleteTimeSheet(String token, String weekEnd) {
        remoteTimeSheetService.deleteTimeSheet(token,weekEnd);
    }

    public void createById(@RequestParam(required=true) String token){
        remoteTimeSheetService.createById(token);
    }


    public TimeSheetDomain getTimeSheet(String token, String weekEnd){
        return  remoteTimeSheetService.getTimeSheet(token,weekEnd).getBody();
    }


    public void saveTimeSheet(MultipartFile file, String json) throws IOException{
        remoteTimeSheetService.saveTimeSheet(file,json);
    }



    public void setDefault(TimeSheetDomain tsd){
        remoteTimeSheetService.setDefault(tsd);
    }




    public String uploadFile(MultipartFile file){
        return remoteTimeSheetService.uploadFile(file).getBody();
    }

    public void createSummary(SummaryDomain sd){
        remoteTimeSheetService.createSummary(sd);
    }

   public List<SummaryDomain> allSummary(String token){
        return remoteTimeSheetService.allSummary(token).getBody();
    }

    public List<SummaryDomain> get5Summary(String token){
        return remoteTimeSheetService.get5Summary(token).getBody();
    }


    public List<SummaryDomain> showMore(List<SummaryDomain> ls,String token){
        return remoteTimeSheetService.showMore(ls,token).getBody();
    }


    public void deleteSummary(){
        remoteTimeSheetService.deleteSummary();
    }
    public TimeSheetDomain edit(String weekEnding,String token){
        return remoteTimeSheetService.edit(weekEnding,token).getBody();
    }

//    @GetMapping("/view")
    public TimeSheetDomain view(String weekEnding,String token){
        return remoteTimeSheetService.view(weekEnding,token).getBody();
    }


}
