package com.example.compositeserver.Controller;


import com.example.compositeserver.Domain.EmployeeService.Employee;
import com.example.compositeserver.Domain.EmployeeService.EmployeeAddrContact;
import com.example.compositeserver.Domain.TimeSheetService.SummaryDomain;
import com.example.compositeserver.Domain.TimeSheetService.TimeSheetDomain;
import com.example.compositeserver.Service.CompositeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("composite-service")
public class CompositeController {

    private static final Logger logger = LoggerFactory.getLogger(CompositeController.class);
    private CompositeService compositeService;

    @Autowired
    public void setCompositeService(CompositeService compositeService) {
        this.compositeService = compositeService;
    }

    @GetMapping("/getAllEmployee")
    public ResponseEntity<List<EmployeeAddrContact>> getAllEmployee(@RequestHeader("Authorization") String token){
        logger.trace("Employee Controller");
        logger.info("Active token: " + token);
        return ResponseEntity.ok(compositeService.getAllEmployees(token));
    }

    @PostMapping("/update-employee/{employeeId}")
    public ResponseEntity<EmployeeAddrContact> updateEmployeeById(@RequestHeader("Authorization") String token, @RequestBody EmployeeAddrContact employeeAddrContact, @PathVariable Integer employeeId){
        logger.trace("Employee Controller");
        logger.info("Active token: " + token);
        return ResponseEntity.ok(compositeService.updateEmployeeById(token, employeeAddrContact, employeeId));
    }

//    @GetMapping("/employee-service/employee-id/{employeeId}")
//    public ResponseEntity<EmployeeAddrContact> findEmployeeById(@RequestHeader("Authorization") String token, @PathVariable Integer employeeId){
//        logger.trace("Employee Controller");
//        logger.info("Active token: " + token);
//        return ResponseEntity.ok(compositeService.findEmployeeById(token, employeeId));
//    }

    @GetMapping("/all")
    public ResponseEntity<List<SummaryDomain>> allSummary(@RequestHeader("Authorization") String token){
        logger.trace("TimeSheet Controller");
        logger.info("Get all summary");
        return ResponseEntity.ok(compositeService.allSummary(token));
    }

    @DeleteMapping("/delete")
    void deleteTimeSheet(@RequestHeader("Authorization") String token, @RequestParam(required=true) String weekEnd){
        compositeService.deleteTimeSheet(token,weekEnd);
    }

    @DeleteMapping("/delete_by_id")
    void createById(@RequestHeader("Authorization") String token) {
        compositeService.createById(token);
    }

    @GetMapping("/get_timesheet")
    ResponseEntity<TimeSheetDomain> getTimeSheet(@RequestHeader("Authorization") String token,@RequestParam(required=true) String weekEnd){
        return ResponseEntity.ok(compositeService.getTimeSheet(token,weekEnd));
    }

    @PutMapping(value = "/save", consumes ={"multipart/form-data"})
    void saveTimeSheet(@RequestPart(name = "file", required = false) MultipartFile file, @RequestParam(name = "json") String json, @RequestHeader("Authorization") String token) throws IOException
    {
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getBytes());
        compositeService.saveTimeSheet(file,json,token);
    }


    @PostMapping("/set_default")
    void setDefault( @RequestBody TimeSheetDomain tsd, @RequestHeader("Authorization") String token) {
        compositeService.setDefault(tsd, token);
    }



    @PostMapping("/upload_test")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(compositeService.uploadFile(file, token));
    }


    @PostMapping("/create")
    void createSummary(@RequestBody SummaryDomain sd, @RequestHeader("Authorization") String token){
        compositeService.createSummary(sd, token);
    }


    @GetMapping("/home")
    ResponseEntity<List<SummaryDomain>> get5Summary(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(compositeService.get5Summary(token));
    }

    @GetMapping("/showmore")
    ResponseEntity<List<SummaryDomain>> showMore(@RequestBody List<SummaryDomain> ls,@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(compositeService.showMore(ls,token));
    }

    @GetMapping("/deleteallsummary")
     void deleteSummary(@RequestHeader("Authorization") String token){
            compositeService.deleteSummary();
            }


    @GetMapping("/edit")
    ResponseEntity<TimeSheetDomain> edit(@RequestParam String weekEnding,@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(compositeService.edit(weekEnding,token));
    }

    @GetMapping("/view")
    ResponseEntity<TimeSheetDomain> view(@RequestParam String weekEnding,@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(compositeService.view(weekEnding, token));
    }
}
