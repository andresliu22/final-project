package com.example.compositeserver.Domain.TimeSheetService;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

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
