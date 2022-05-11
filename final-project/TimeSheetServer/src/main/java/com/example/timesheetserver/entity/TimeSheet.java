package com.example.timesheetserver.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection="timesheet")

public class TimeSheet {
    @Id
    @Generated
    private String id;


    private int userid;

    private List<String> days;

    private double totalBillingHours;

    private double totalCompensatedHours;

    private String approvalStatus;

    private String submissionStatus;

    private int floatingDaysWeek;

    private int vocationDaysWeek;

    private String filePath;

    private String weekEnd;


    @PersistenceConstructor
    public TimeSheet(int userid, List<String> days, double totalBillingHours, double totalCompensatedHours, String approvalStatus, String submissionStatus,
                     int floatingDaysWeek, int vocationDaysWeek, String filePath,String weekEnd) {
        this.userid = userid;
        this.days = days;
        this.totalBillingHours = totalBillingHours;
        this.totalCompensatedHours = totalCompensatedHours;
        this.approvalStatus = approvalStatus;
        this.submissionStatus = submissionStatus;
        this.floatingDaysWeek = floatingDaysWeek;
        this.vocationDaysWeek = vocationDaysWeek;
        this.filePath = filePath;
        this.weekEnd=weekEnd;
    }




}
