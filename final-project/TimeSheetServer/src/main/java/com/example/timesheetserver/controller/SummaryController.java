package com.example.timesheetserver.controller;

import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.timesheetserver.security.*;

import java.util.List;

@RestController
@RequestMapping("summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;


    @PostMapping("/create")
    public void createSummary(@RequestBody SummaryDomain sd){
        summaryService.createSummary(sd);
    };

    @GetMapping("/all")
    public ResponseEntity allSummary(){
        return ResponseEntity.ok(summaryService.getAllSummaries());
    }

    @GetMapping("/home")
    public ResponseEntity get5Summary(@RequestParam String jwt){

        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        List<SummaryDomain> list=summaryService.get5summaries(userid);
        if(list!=null) {
            return ResponseEntity.ok(list);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("showmore")
    public ResponseEntity showMore(@RequestBody List<SummaryDomain> ls,@RequestParam String jwt){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        return ResponseEntity.ok(summaryService.ShowMore(ls,userid));
    }

    @GetMapping("/delete")
    public void deleteSummary(){
        summaryService.deleteAll();
    };


    @GetMapping("/edit")
    public ResponseEntity edit(@RequestParam String weekEnding,String jwt){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        return ResponseEntity.ok(summaryService.edit(weekEnding,userid));
    }

    @GetMapping("/view")
    public ResponseEntity view(@RequestParam String weekEnding,String jwt){
        String u=JwtUtil.getSubjectFromJwt(jwt);
        int userid=Integer.parseInt(u);
        return ResponseEntity.ok(summaryService.view(weekEnding,userid));
    }
}

