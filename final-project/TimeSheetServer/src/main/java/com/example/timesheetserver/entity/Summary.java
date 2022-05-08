package com.example.timesheetserver.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection="summary")
public class Summary {


    @Id
    @Generated
    private String id;
    private String weekEnding;
    private int totalHours;
    private String submissionStatus;
    private String approvalStatues;
    private String option;
    private String comment;
}
