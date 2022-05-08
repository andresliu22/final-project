package com.example.timesheetserver.service;

import com.example.timesheetserver.dao.DayRepo;
import com.example.timesheetserver.dao.TimesheetRepo;
import com.example.timesheetserver.domain.DayDomain;
import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.domain.TimeSheetDomain;
import com.example.timesheetserver.entity.Day;
import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSheetService {
    @Autowired
    private DayRepo dayRepo;

    @Autowired
    private TimesheetRepo timesheetRepo;

//    @Transactional
//    public void createSummary();


//    @Transactional(readOnly=true)
//    public List<SummaryDomain> getSummary(String WeekEnd, int userid) {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Optional<TimeSheet> ts = timesheetRepo.findFirst5ByWeekEnd(WeekEnd, Sort.sort());
//
//        if (ts.isPresent()) {
//            Product product = productOptional.get();
//            return List<SummaryDomain>;
//        }
//
//        throw new RuntimeException("No product found");
//    }

    /*@Transactional(readOnly=true)
    public List<SummarYDomain> getFiveMore( int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" name");
        Optional<Product> productOptional = productRepo.findByName(name);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return List<SummaryDomain>;
        }

        throw new RuntimeException("No product found");
    }*/

    @Transactional
    public void deleteTimeSheetById(String tsId) {
        timesheetRepo.deleteById(tsId);
    }



    @Transactional(readOnly=true)
    public TimeSheetDomain getTimeSheetDomain(String weekEnd, int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("before dao");
        TimeSheet ts = timesheetRepo.findByWeekEnd(weekEnd);

        if (ts != null) {
            return timesheetToDomain(ts);
        }

        throw new RuntimeException("No product found");
    }

    /*@Transactional(readOnly=true)
    public List<dayDomain> getDays(String weekEnd, int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" name");
        Optional<Product> productOptional = productRepo.findByName(name);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return List<SummaryDomain>;
        }

        throw new RuntimeException("No product found");
    }*/



    @Transactional
    public void createTimeSheet(TimeSheetDomain timesheetDomain) {
        System.out.println("enter here!");
        List<String> daysid = timesheetDomain.getDays().stream().map(d->{
                Day newday = new Day();
                newday.setDate(d.getDate());
                newday.setDay(d.getDay());
                newday.setIsFloating(d.getIsFloating());
                newday.setIsHoliday(d.getIsHoliday());
                newday.setIsVacation(d.getIsVacation());
                newday.setStartTime(d.getStartTime());
                newday.setEndTime(d.getEndTime());

                Day afterInsert = dayRepo.insert(newday);
                return afterInsert.getId();
            }).collect(Collectors.toList());

//        List<String> newid = daysid.stream()
//                .map(String::valueOf)
//                .collect(Collectors.toList());

        TimeSheet ts = new TimeSheet();
        ts.setUserid(timesheetDomain.getUserid());
        ts.setTotalBillingHours(timesheetDomain.getTotalBillingHours());
        ts.setTotalCompensatedHours(timesheetDomain.getTotalCompensatedHours());
        ts.setDays(daysid);
        ts.setApprovalStatus(timesheetDomain.getApprovalStatus());
        ts.setSubmissionStatus(timesheetDomain.getSubmissionStatus());
        ts.setWeekEnd(timesheetDomain.getWeekEnd());
        ts.setFloatingDaysWeek(timesheetDomain.getFloatingDaysWeek());
        ts.setVocationDaysWeek(timesheetDomain.getVocationDaysWeek());
        ts.setFilePath(timesheetDomain.getFilePath());

        timesheetRepo.insert(ts);
    }





    // Convert timesheet entity  to domain
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
                .build();
    }




}
