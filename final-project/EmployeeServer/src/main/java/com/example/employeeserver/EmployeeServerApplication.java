package com.example.employeeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class EmployeeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeServerApplication.class, args);
    }

}
