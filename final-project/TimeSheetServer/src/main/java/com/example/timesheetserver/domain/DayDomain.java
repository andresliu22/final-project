package com.example.timesheetserver.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class DayDomain {
    private String date;
    private String day;
    private String endTime;
    private Boolean isFloating;
    private Boolean isHoliday;
    private Boolean isVacation;
    private String startTime;
}
