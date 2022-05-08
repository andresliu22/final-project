package com.example.compositeserver.Controller;


import com.example.compositeserver.Service.CompositeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("composite-service")
public class CompositeController {

    private CompositeService compositeService;

    @Autowired
    public void setCompositeService(CompositeService compositeService) {
        this.compositeService = compositeService;
    }

    @GetMapping("getAllEmployee")
    public ResponseEntity getAllEmployee(){
        return ResponseEntity.ok(compositeService.getAllEmployees());
    }
    // how can we get header?
}
