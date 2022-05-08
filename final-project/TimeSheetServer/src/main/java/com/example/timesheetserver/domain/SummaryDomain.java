package com.example.timesheetserver.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SummaryDomain {
    private String weekEnding;
    private int totalHours;
    private String submissionStatus;
    private String approvalStatues;
    private String option;
    private String comment;
}
