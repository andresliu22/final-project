package com.example.timesheetserver.service;


import com.example.timesheetserver.dao.DayRepo;
import com.example.timesheetserver.dao.SummaryRepo;
import com.example.timesheetserver.dao.TimesheetRepo;
import com.example.timesheetserver.domain.DayDomain;
import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.domain.TimeSheetDomain;
import com.example.timesheetserver.entity.Day;
import com.example.timesheetserver.entity.Summary;
import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SummaryService {
    @Autowired
    private SummaryRepo smRepo;

    @Autowired
    private TimesheetRepo timesheetRepo;

    @Autowired
    private DayRepo dayRepo;

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
    public List<SummaryDomain> get5summaries(int userid){

        List<SummaryDomain> lst = smRepo.findTop5ByOrderByWeekEndingDesc().stream()
                .map(p->summaryToDomain(p)).collect(Collectors.toList());
        return show(lst,userid);
    }

    @Transactional
    public void deleteAll(){
        smRepo.deleteAll();
    }


    @Transactional
    public List<SummaryDomain> ShowMore(List<SummaryDomain> ls,int userid){
        LocalDate date= weekEndingToLocaldate(ls.get(ls.size()-1).getWeekEnding());
        LocalDate cDate=date.minusWeeks(6);
        Summary summary=new Summary();

        List<SummaryDomain> res = smRepo.findAllByOrderByWeekEndingDesc().stream().filter((s)->weekEndingToLocaldate(s.getWeekEnding()).isAfter(cDate))
                .map(p->summaryToDomain(p)).collect(Collectors.toList());
        return show(res,userid);
    }

    @Transactional
    public TimeSheetDomain view(String weekEnding,int userid){
        TimeSheet res=timesheetRepo.findByWeekEndAndUserid(weekEnding,userid);
        return timesheetToDomain(res);
    }


    @Transactional
    public TimeSheetDomain edit(String weekEnding,int userid){
        TimeSheet ts=timesheetRepo.findByWeekEndAndUserid(weekEnding,userid);
        Summary smd=smRepo.findByWeekEnding(weekEnding);

        if(ts!=null){

            return timesheetToDomain(ts);
        }
        else{

            TimeSheet defau=timesheetRepo.findByWeekEndAndUserid("00/00/0000",userid);
            if(defau!=null){
                return TimeSheetDomain.builder().totalBillingHours(defau.getTotalBillingHours()).
                        approvalStatus(defau.getApprovalStatus())
                        .totalCompensatedHours(defau.getTotalCompensatedHours())
                        .days(timesheetToDomain(defau).getDays()).
                        floatingDaysWeek(defau.getFloatingDaysWeek()).
                        weekEnd(defau.getWeekEnd())
                        .userid(defau.getUserid()).build();
            }
            else{
                return createTimeSheet(summaryToDomain(smd),userid);
            }
        }
    }



    public LocalDate weekEndingToLocaldate(String weekE){
        String[] format=weekE.split("/");
        StringBuilder sb=new StringBuilder();
        sb.append(format[2]+"-");
        sb.append(format[0]+"-");
        sb.append(format[1]);
        return LocalDate.parse(sb.toString());
    }
    public String localdateToWeekEnding(LocalDate d){
        String date=d.toString();
        Summary sm=new Summary();
        String[] format=date.split("-");
        StringBuilder sb=new StringBuilder();
        sb.append(format[1]+"/");
        sb.append(format[2]+"/");
        sb.append(format[0]);
        date=sb.toString();
        return date;
    }


    public List<SummaryDomain> show(List<SummaryDomain> sml,int userid){
        List<SummaryDomain> res=new ArrayList<>();
        for(SummaryDomain sm:sml){
        String weekEnding=sm.getWeekEnding();
        TimeSheet ts=timesheetRepo.findByWeekEndAndUserid(weekEnding,userid);
        if(ts==null){
            res.add(sm);
            continue;
        }
        else {
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
            res.add(sm);
        }

        }
        return res;
    }

    TimeSheetDomain timesheetToDomain(TimeSheet ts) {
        List<String> days = ts.getDays();

        List<DayDomain> dayDomains = days.stream()
                .map(id->dayRepo.findById(id))
                .filter(o->o.isPresent())
                .map(o->{
                    Day d = o.get();
                    return DayDomain.builder()
                            .date(d.getDate())
                            .day(d.getDay())
                            .isFloating(d.getIsFloating())
                            .isHoliday(d.getIsHoliday())
                            .isVacation(d.getIsVacation())
                            .startTime(d.getStartTime())
                            .endTime(d.getEndTime())
                            .build();
                }).collect(Collectors.toList());

        return TimeSheetDomain.builder()
                .userid(ts.getUserid())
                .days(dayDomains)
                .totalBillingHours(ts.getTotalBillingHours())
                .totalCompensatedHours(ts.getTotalCompensatedHours())
                .approvalStatus(ts.getApprovalStatus())
                .submissionStatus(ts.getSubmissionStatus())
                .floatingDaysWeek(ts.getFloatingDaysWeek())
                .vocationDaysWeek(ts.getVocationDaysWeek())
                .filePath(ts.getFilePath())
                .weekEnd(ts.getWeekEnd())
                .build();
    }

    public TimeSheetDomain createTimeSheet(SummaryDomain sm,int userid){
        String weekEnding=sm.getWeekEnding();
        List<DayDomain> days=new ArrayList<>();
        String[] weekday={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        LocalDate date=weekEndingToLocaldate(weekEnding);

        for(int i=0;i<7;i++){
            String startingTime="9：00 A.M.";
            String endingTime="6:00 P.M.";
            if(i==0||i==6){
                startingTime="N/A";
                endingTime="N/A";
            }

            DayDomain dd=DayDomain.builder().day(weekday[i]).date(localdateToWeekEnding(date.minusDays(6).plusDays(i)))
                    .endTime(endingTime)
                    .startTime(startingTime).isFloating(false).isHoliday(false).isVacation(false).build();
            days.add(dd);
        }
        TimeSheetDomain tsd=TimeSheetDomain.builder().
                userid(userid).
                totalCompensatedHours(sm.getTotalHours()).
                totalBillingHours(sm.getTotalHours()).
                weekEnd(weekEnding).
                days(days).
                floatingDaysWeek(0).
                vocationDaysWeek(0).
                approvalStatus("Not approved").
                submissionStatus("Incomplete").build();
        return tsd;
    }




}
