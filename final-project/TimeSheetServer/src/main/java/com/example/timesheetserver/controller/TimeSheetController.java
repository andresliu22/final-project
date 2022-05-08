package com.example.timesheetserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.timesheetserver.service.TimeSheetService;
import com.example.timesheetserver.domain.TimeSheetDomain;

@RestController
@RequestMapping("/timeSheet")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetService;


//    @GetMapping("/Create")
//    public ResponseEntity createSummary(){
//
//    };

    @PostMapping("/create_timesheet")
    public ResponseEntity createProduct(@RequestBody  TimeSheetDomain tsd) {
        System.out.println("success before");
        System.out.println(tsd.getDays());
        System.out.println("success after");
        timeSheetService.createTimeSheet(tsd);
        return new ResponseEntity(tsd, HttpStatus.CREATED);
    }

}
