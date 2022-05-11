package com.example.timesheetserver.domain;

import lombok.*;

@Data
@Builder
@ToString

public class SummaryDomain {
    private String weekEnding;
    private double totalHours;
    private String submissionStatus;
    private String approvalStatus;
    private String option;
    private String comment;
}
