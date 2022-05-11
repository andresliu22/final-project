package com.example.compositeserver.Service.RemoteService;


import com.example.compositeserver.Domain.TimeSheetService.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FeignClient("timesheet-service")

public interface RemoteTimeSheetService {
    @PostMapping("timesheet-service/create_timesheet")
     ResponseEntity<TimeSheetDomain> createProduct(@RequestBody TimeSheetDomain tsd) ;


    @DeleteMapping("timesheet-service/delete")
     ResponseEntity deleteTimeSheet(@RequestParam(required=true) String jwt, @RequestParam(required=true) String weekEnd) ;

    @DeleteMapping("timesheet-service/delete_by_id")
     ResponseEntity createById(@RequestParam(required=true) String jwt) ;

    @GetMapping("timesheet-service/get_timesheet")
     ResponseEntity<TimeSheetDomain> getTimeSheet(@RequestParam(required=true) String jwt, @RequestParam(required=true) String weekEnd);

    @PutMapping("timesheet-service/save")
     ResponseEntity saveTimeSheet(@RequestParam(name = "file",required = false) MultipartFile file, @RequestParam(name = "json") String json) throws IOException;


    @PostMapping("timesheet-service/set_default")
     ResponseEntity setDefault( @RequestBody TimeSheetDomain tsd) ;



    @PostMapping("timesheet-service/upload_test")
     ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) ;


    @PostMapping("timesheet-service/create")
     void createSummary(@RequestBody SummaryDomain sd);

    @GetMapping("timesheet-service/all")
     ResponseEntity<List<SummaryDomain>> allSummary();

    @GetMapping("timesheet-service/home")
     ResponseEntity<List<SummaryDomain>> get5Summary(@RequestParam String jwt);

    @PostMapping("timesheet-service/showmore")
     ResponseEntity<List<SummaryDomain>> showMore(@RequestBody List<SummaryDomain> ls,@RequestParam String jwt);

    @GetMapping("timesheet-service/deleteallsummary")
     void deleteSummary();


    @GetMapping("timesheet-service/edit")
     ResponseEntity<TimeSheetDomain> edit(@RequestParam String weekEnding,String jwt);

    @GetMapping("timesheet-service/view")
     ResponseEntity<TimeSheetDomain> view(@RequestParam String weekEnding,String jwt);
}