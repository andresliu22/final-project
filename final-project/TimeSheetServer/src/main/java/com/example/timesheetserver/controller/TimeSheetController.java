package com.example.timesheetserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.timesheetserver.service.TimeSheetService;

@RestController
@RequestMapping("timeSheet")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetService;


    @GetMapping("/Create")
    public ResponseEntity createSummary(){

    };
}
