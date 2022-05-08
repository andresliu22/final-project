package com.example.timesheetserver.controller;

import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity get5Summary(){
        List<SummaryDomain> list=summaryService.get5summaries();
        if(list!=null) {
            return ResponseEntity.ok(list);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/delete")
    public void deleteSummary(){
        summaryService.deleteAll();
    };
}

