package com.example.timesheetserver.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Builder
@ToString
@Getter
public class SummaryDomain {
    private String weekEnding;
    private double totalHours;
    private String submissionStatus;
    private String approvalStatus;
    private String option;
    private String comment;
}
