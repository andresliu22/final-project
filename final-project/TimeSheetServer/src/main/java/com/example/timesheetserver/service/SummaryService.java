package com.example.timesheetserver.service;


import com.example.timesheetserver.dao.SummaryRepo;
import com.example.timesheetserver.dao.TimesheetRepo;
import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.domain.TimeSheetDomain;
import com.example.timesheetserver.entity.Summary;
import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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


    @Transactional
    public List<SummaryDomain> ShowMore(List<SummaryDomain> ls){
        LocalDate date= weekEndingToLocaldate(ls.get(ls.size()-1).getWeekEnding());
        LocalDate cDate=date.minusWeeks(6);
        Summary summary=new Summary();
        List<SummaryDomain> res = smRepo.findAllByOrderByWeekEndingDesc().stream().filter((s)->weekEndingToLocaldate(s.getWeekEnding()).isAfter(cDate))
                .map(p->summaryToDomain(p)).collect(Collectors.toList());
        return res;
    }


    @Transactional
    public TimeSheet edit(String weekEnding){
        TimeSheet ts=timesheetRepo.findByWeekEnd(weekEnding);
        if(TimeSheet)
    }



    public LocalDate weekEndingToLocaldate(String weekE){
        String[] format=weekE.split("/");
        StringBuilder sb=new StringBuilder();
        sb.append(format[2]+"-");
        sb.append(format[0]+"-");
        sb.append(format[1]);
        return LocalDate.parse(sb.toString());

    }

    public Summary generateSummary(Summary sm,int userid){
        String weekEnding=sm.getWeekEnding();
        TimeSheet ts=timesheetRepo.findByWeekEnd(weekEnding);
        if(ts==null){
            return sm;
        }
        else{
            int fl=ts.getFloatingDaysWeek();
            int vo=ts.getVocationDaysWeek();
            StringBuilder sb=new StringBuilder();
            if(fl!=0){
                sb.append(fl+" floating day(s) required");
            }
            if(vo!=0){
                sb.append(vo+" vacation day(s) required");
            }

            sm.setComment(sb.toString());
            sm.setApprovalStatues(ts.getApprovalStatus());
            sm.setTotalHours(ts.getTotalBillingHours());
            if(sm.getApprovalStatues().equalsIgnoreCase("approved")){
                sm.setOption("view");
            }
            sm.setSubmissionStatus(ts.getSubmissionStatus());
            return sm;
        }
    }


}
