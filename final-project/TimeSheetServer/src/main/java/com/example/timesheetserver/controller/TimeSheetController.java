package com.example.timesheetserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.timesheetserver.service.TimeSheetService;
import com.example.timesheetserver.domain.TimeSheetDomain;

import javax.servlet.http.HttpServletRequest;

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

    @DeleteMapping("/delete")
    public ResponseEntity deleteTimeSheet(@RequestParam(required=true) Integer userid, @RequestParam(required=true) String weekEnd) {
        timeSheetService.deleteTimeSheet(userid, weekEnd);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete_by_id")
    public ResponseEntity createById(@RequestParam(required=true) Integer userid) {
        timeSheetService.deleteTimeSheetById(userid);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/get_timesheet")
    public ResponseEntity<TimeSheetDomain> getTimeSheet(@RequestParam(required=true) Integer userid, @RequestParam(required=true) String weekEnd){
        TimeSheetDomain tsd = timeSheetService.getTimeSheetDomain(weekEnd,userid);
        return ResponseEntity.ok().body(tsd);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody TimeSheetDomain tsd){
        timeSheetService.saveTimeSheet(tsd);
        return new ResponseEntity(HttpStatus.OK);
    }

//    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No Pet is found")
//    @ExceptionHandler(NoPetFoundException.class)
//    public void noPetFound(HttpServletRequest req, NoPetFoundException e) {
//        LOGGER.warn("URI: "+req.getRequestURI()+", NoPetFoundException: "+e.getMessage());
//    }
}
