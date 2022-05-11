package com.example.compositeserver.Domain.TimeSheetService;

import lombok.Data;

@Data
public class HolidayDomain {
    private String name;


    private String date;


    private String observed;

    private Boolean isPublic;
}
