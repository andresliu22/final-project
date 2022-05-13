package com.example.timesheetserver.service;


import com.example.timesheetserver.dao.SummaryRepo;
import com.example.timesheetserver.dao.TimesheetRepo;
import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.entity.Summary;
import com.example.timesheetserver.entity.TimeSheet;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AsyncTimeSheetService {

    @Autowired
    private SummaryRepo smRepo;

    @Autowired
    private TimesheetRepo timesheetRepo;

    @Autowired
    private TimeSheetService tmservice;

    @Async("customExecutor")
    public CompletableFuture<SummaryDomain> show(SummaryDomain sm,int userid){
        String weekEnding=sm.getWeekEnding();
        TimeSheet ts=timesheetRepo.findByWeekEndAndUserid(weekEnding,userid);

        if(ts!=null) {
            int fl = ts.getFloatingDaysWeek();
            int vo = ts.getVocationDaysWeek();
            StringBuilder sb = new StringBuilder();
            if (fl != 0) {
                sb.append(fl + " floating day(s) required");
            }
            if (vo != 0) {
                sb.append(vo + " vacation day(s) required");
            }

            sm.setComment(sb.toString());
            sm.setApprovalStatus(ts.getApprovalStatus());
            sm.setTotalHours(ts.getTotalBillingHours());
            if (sm.getApprovalStatus().equalsIgnoreCase("approved")) {
                sm.setOption("view");
            }
            sm.setSubmissionStatus(ts.getSubmissionStatus());
        }
            return CompletableFuture.completedFuture(sm);
    }

}
