package com.example.timesheetserver.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
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

    private double totalHours;
    private String submissionStatus;
    private String approvalStatues;
    private String option;
    private String comment;

    @PersistenceConstructor
    public Summary(String id, String weekEnding,double totalHours,String submissionStatus,String approvalStatues,String option,String comment) {
        this.id=id;
        this.weekEnding=weekEnding;
        this.submissionStatus=submissionStatus;
        this.approvalStatues=approvalStatues;
        this.option=option;
        this.totalHours=totalHours;
        this.comment=comment;
    }
}

