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
@Document(collection="timeSheet")

public class timeSheet {
    @Id
    @Generated
    private String id;

    private int userid;

    private List<String> days;

    private int totalBillingHours;

    private int totalCompensatedHours;

    private String approvalStatus;

    private String submissionStatus;

    private int floatingDaysWeek;

    private int vocationDaysWeek;

    private String filePath;


    @PersistenceConstructor
    public timeSheet(int userid,List<String> days,int totalBillingHours,int totalCompensatedHours, String approvalStatus,String submissionStatus,
                     int floatingDaysWeek,int vocationDaysWeek,String filePath) {
        this.userid = userid;
        this.days = days;
        this.totalBillingHours = totalBillingHours;
        this.totalCompensatedHours = totalCompensatedHours;
        this.approvalStatus = approvalStatus;
        this.submissionStatus = submissionStatus;
        this.floatingDaysWeek = floatingDaysWeek;
        this.vocationDaysWeek = vocationDaysWeek;
        this.filePath = filePath;
    }




}
