package com.example.timesheetserver.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TimeSheetDomain {

    private int userid;

    private List<DayDomain> days;

    private int totalBillingHours;

    private int totalCompensatedHours;

    private String approvalStatus;

    private String submissionStatus;

    private int floatingDaysWeek;

    private int vocationDaysWeek;

    private String filePath;

}
