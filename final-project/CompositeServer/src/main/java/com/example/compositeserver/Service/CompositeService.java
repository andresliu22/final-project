package com.example.compositeserver.Service;


import com.example.compositeserver.Domain.EmployeeService.Employee;
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

    public List<Employee> getAllEmployees(String token){
        List<Employee> EmployeeList = remoteEmployeeService.getAllEmployees(token).getBody();
        return EmployeeList;
    }

    public TimeSheetDomain createProduct(TimeSheetDomain tsd){
        TimeSheetDomain res=remoteTimeSheetService.createProduct(tsd).getBody();
        return res;
    }



    public void deleteTimeSheet(String jwt, String weekEnd) {
        remoteTimeSheetService.deleteTimeSheet(jwt,weekEnd);
    }

    public void createById(@RequestParam(required=true) String jwt){
        remoteTimeSheetService.createById(jwt);
    }


    public TimeSheetDomain getTimeSheet(String jwt, String weekEnd){
        return  remoteTimeSheetService.getTimeSheet(jwt,weekEnd).getBody();
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

   public List<SummaryDomain> allSummary(){
        return remoteTimeSheetService.allSummary().getBody();
    }

    public List<SummaryDomain> get5Summary(String jwt){
        return remoteTimeSheetService.get5Summary(jwt).getBody();
    }


    public List<SummaryDomain> showMore(List<SummaryDomain> ls,String jwt){
        return remoteTimeSheetService.showMore(ls,jwt).getBody();
    }


    public void deleteSummary(){
        remoteTimeSheetService.deleteSummary();
    }
//
//

    public TimeSheetDomain edit(String weekEnding,String jwt){
        return remoteTimeSheetService.edit(weekEnding,jwt).getBody();
    }

//    @GetMapping("/view")
    public TimeSheetDomain view(String weekEnding,String jwt){
        return remoteTimeSheetService.view(weekEnding,jwt).getBody();
    }


}
