package com.example.compositeserver.Controller;


import com.example.compositeserver.Domain.TimeSheetService.SummaryDomain;
import com.example.compositeserver.Domain.TimeSheetService.TimeSheetDomain;
import com.example.compositeserver.Service.CompositeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("composite-service")
public class CompositeController {

    private CompositeService compositeService;

    @Autowired
    public void setCompositeService(CompositeService compositeService) {
        this.compositeService = compositeService;
    }

    @GetMapping("/getAllEmployee")
    public ResponseEntity getAllEmployee(){
        return ResponseEntity.ok(compositeService.getAllEmployees());
    }
    // how can we get header?

    @GetMapping("/all")
    public ResponseEntity<List<SummaryDomain>> allSummary(){
        return ResponseEntity.ok(compositeService.allSummary());
    }

    @DeleteMapping("/delete")
    void deleteTimeSheet(@RequestParam(required=true) String jwt, @RequestParam(required=true) String weekEnd){
        compositeService.deleteTimeSheet(jwt,weekEnd);
    }

    @DeleteMapping("/delete_by_id")
    void createById(@RequestParam(required=true) String jwt) {
        compositeService.createById(jwt);
    }

    @GetMapping("/get_timesheet")
    ResponseEntity<TimeSheetDomain> getTimeSheet(@RequestParam(required=true) String jwt, @RequestParam(required=true) String weekEnd){
        return ResponseEntity.ok(compositeService.getTimeSheet(jwt,weekEnd));
    }

    @PutMapping("/save")
    void saveTimeSheet(@RequestParam(name = "file",required = false) MultipartFile file, @RequestParam(name = "json") String json) throws IOException
    {
        compositeService.saveTimeSheet(file,json);
    }


    @PostMapping("/set_default")
    void setDefault( @RequestBody TimeSheetDomain tsd) {
        compositeService.setDefault(tsd);
    }



    @PostMapping("/upload_test")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(compositeService.uploadFile(file));
    }


    @PostMapping("/create")
    void createSummary(@RequestBody SummaryDomain sd){
        compositeService.createSummary(sd);
    }


    @GetMapping("/home")
    ResponseEntity<List<SummaryDomain>> get5Summary(@RequestParam String jwt){
        return ResponseEntity.ok(compositeService.get5Summary(jwt));
    }

    @GetMapping("/showmore")
    ResponseEntity<List<SummaryDomain>> showMore(@RequestBody List<SummaryDomain> ls,@RequestParam String jwt){
        return ResponseEntity.ok(compositeService.showMore(ls,jwt));
    }

    @GetMapping("/deleteallsummary")
     void deleteSummary(){
            compositeService.deleteSummary();
            }


    @GetMapping("/edit")
    ResponseEntity<TimeSheetDomain> edit(@RequestParam String weekEnding,String jwt){
        return ResponseEntity.ok(compositeService.edit(weekEnding,jwt));
    }

    @GetMapping("/view")
    ResponseEntity<TimeSheetDomain> view(@RequestParam String weekEnding,String jwt){
        return ResponseEntity.ok(compositeService.view(weekEnding, jwt));
    }
}
