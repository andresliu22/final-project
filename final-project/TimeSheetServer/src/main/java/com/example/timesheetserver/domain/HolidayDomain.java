package com.example.timesheetserver.domain;

import lombok.Data;

@Data
public class HolidayDomain {
    private String name;


    private String date;


    private String observed;

    private Boolean isPublic;
}
