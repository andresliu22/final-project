package com.example.timesheetserver.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import com.example.timesheetserver.domain.DayDomain;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;

@Data
@Builder

public class TimeSheetDomain {

    private int userid;

    private List<DayDomain> days;

    private double totalBillingHours;

    private double totalCompensatedHours;

    private String approvalStatus;

    private String submissionStatus;

    private int floatingDaysWeek;

    private int vocationDaysWeek;

    private String filePath;

    private String weekEnd;



}
