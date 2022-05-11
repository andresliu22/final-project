package com.example.timesheetserver.controller;


import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.entity.User;
import com.example.timesheetserver.security.JwtUtil;
import com.example.timesheetserver.service.AmazonS3Service;
import com.example.timesheetserver.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import com.example.timesheetserver.service.TimeSheetService;
import com.example.timesheetserver.domain.TimeSheetDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/timesheet-service")
public class TimeSheetController {

    @Autowired
    private TimeSheetService timeSheetService;

    @Autowired
    private AmazonS3Service s3Service;
//    @GetMapping("/Create")
//    public ResponseEntity createSummary(){
//
//    };

    @Autowired
    private UserService userService;

    Gson gson = new Gson();


    @PostMapping("/create_timesheet")
    public ResponseEntity<TimeSheetDomain> createProduct(@RequestBody  TimeSheetDomain tsd) {
        System.out.println("success before");
        System.out.println(tsd.getDays());
        System.out.println("success after");
        timeSheetService.createTimeSheet(tsd);
        return new ResponseEntity(tsd, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteTimeSheet(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required=true) String weekEnd) {
//        String u=JwtUtil.getSubjectFromJwt(jwt);
//        int userid=Integer.parseInt(u);
        User currentUser = (User) userService.loadUserByUsername(userDetails.getUsername());
        timeSheetService.deleteTimeSheet(currentUser.getUserId(), weekEnd);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete_by_id")
    public ResponseEntity createById(@RequestParam(required=true) String jwt) {
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        timeSheetService.deleteTimeSheetById(userid);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/get_timesheet")
    public ResponseEntity<TimeSheetDomain> getTimeSheet(@RequestParam(required=true) String jwt, @RequestParam(required=true) String weekEnd){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        TimeSheetDomain tsd = timeSheetService.getTimeSheetDomain(weekEnd,userid);
        return ResponseEntity.ok().body(tsd);
    }

    @PutMapping("/save")
    public ResponseEntity saveTimeSheet(@RequestParam(name = "file",required = false) MultipartFile file, @RequestParam(name = "json") String json) throws IOException {
        TimeSheetDomain tsd =  gson.fromJson(json, TimeSheetDomain.class);
        timeSheetService.saveTimeSheet(file, tsd);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/set_default")
    public ResponseEntity setDefault( @RequestBody TimeSheetDomain tsd) {
        timeSheetService.setDefault(tsd);
        return new ResponseEntity(HttpStatus.OK);
    }



    @PostMapping("/upload_test")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<String>(s3Service.uploadFile(file), HttpStatus.OK);

    }


    @PostMapping("/create")
    public void createSummary(@RequestBody SummaryDomain sd){
        timeSheetService.createSummary(sd);
    };

    @GetMapping("/all")
    public ResponseEntity<List<SummaryDomain>> allSummary(){
        return ResponseEntity.ok(timeSheetService.getAllSummaries());
    }

    @GetMapping("/home")
    public ResponseEntity<List<SummaryDomain>> get5Summary(@RequestParam String jwt){

        String u= JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        List<SummaryDomain> list=timeSheetService.get5summaries(userid);
        if(list!=null) {
            return ResponseEntity.ok(list);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("showmore")
    public ResponseEntity<List<SummaryDomain>> showMore(@RequestBody List<SummaryDomain> ls,@RequestParam String jwt){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        return ResponseEntity.ok(timeSheetService.ShowMore(ls,userid));
    }

    @GetMapping("/deleteallsummary")
    public void deleteSummary(){
        timeSheetService.deleteAll();
    };


    @GetMapping("/edit")
    public ResponseEntity<TimeSheetDomain> edit(@RequestParam String weekEnding,String jwt){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        return ResponseEntity.ok(timeSheetService.edit(weekEnding,userid));
    }

    @GetMapping("/view")
    public ResponseEntity<TimeSheetDomain> view(@RequestParam String weekEnding,String jwt){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        return ResponseEntity.ok(timeSheetService.view(weekEnding,userid));
    }

//    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No Pet is found")
//    @ExceptionHandler(NoPetFoundException.class)
//    public void noPetFound(HttpServletRequest req, NoPetFoundException e) {
//        LOGGER.warn("URI: "+req.getRequestURI()+", NoPetFoundException: "+e.getMessage());
//    }
}
