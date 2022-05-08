package com.example.timesheetserver.service;


import com.example.timesheetserver.dao.SummaryRepo;
import com.example.timesheetserver.dao.TimesheetRepo;
import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.entity.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SummaryService {
    @Autowired
    private SummaryRepo smRepo;

    @Autowired
    private TimesheetRepo timesheetRepo;

    @Transactional
    public void createSummary(SummaryDomain sd){
        Summary su=new Summary();
        su.setSubmissionStatus(sd.getSubmissionStatus());
        su.setComment(sd.getComment());
        su.setApprovalStatues(sd.getApprovalStatus());
        su.setOption(sd.getOption());
        su.setTotalHours(sd.getTotalHours());
        su.setWeekEnding(sd.getWeekEnding());

        smRepo.insert(su);

    }



    @Transactional
    public List<SummaryDomain> getAllSummaries() {
        List<SummaryDomain> lst = smRepo.findAll().stream()
                .map(p->summaryToDomain(p)).collect(Collectors.toList());

        return lst;
    }


    @Transactional
    public SummaryDomain summaryToDomain(Summary sm){
        return SummaryDomain.builder().
                approvalStatus(sm.getApprovalStatues()).
                submissionStatus(sm.getSubmissionStatus()).
                comment(sm.getComment())
                .option(sm.getOption()).
                weekEnding(sm.getWeekEnding()).
                totalHours(sm.getTotalHours()).build();
    }

    @Transactional
    public List<SummaryDomain> get5summaries(){
        List<SummaryDomain> lst = smRepo.findTop5ByOrderByWeekEndingDesc().stream()
                .map(p->summaryToDomain(p)).collect(Collectors.toList());

        return lst;
    }

    @Transactional
    public void deleteAll(){
        smRepo.deleteAll();
    }

}
