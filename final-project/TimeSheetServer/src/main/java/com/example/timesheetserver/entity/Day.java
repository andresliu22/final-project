package com.example.timesheetserver.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection="day")
public class Day {

    @Id
    @Generated
    private String id;

    private String date;
    private String day;
    private String endTime;
    private Boolean isFloating;
    private Boolean isHoliday;
    private Boolean isVacation;
    private String startTime;


    @PersistenceConstructor
    public Day(String date, String day, String endTime, Boolean isFloating, Boolean isHoliday, Boolean isVacation, String startTime) {
        this.date=date;
        this.day=day;
        this.endTime=endTime;
        this.isFloating=isFloating;
        this.isHoliday=isHoliday;
        this.isVacation=isVacation;
        this.startTime=startTime;
    }
}
