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
import com.example.timesheetserver.service.AmazonS3Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.DecimalFormat;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Service
public class TimeSheetService {
    @Autowired
    private DayRepo dayRepo;

    @Autowired
    private TimesheetRepo timesheetRepo;

    @Autowired
    private AmazonS3Service s3Service;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Transactional
    public void deleteTimeSheet(int userId, String weekEnd) {
        TimeSheet ts = timesheetRepo.findByWeekEndAndUserid(weekEnd, userId);
        for (String d : ts.getDays()){
            dayRepo.deleteById(d);
        }
        timesheetRepo.deleteByUseridAndWeekEnd(userId, weekEnd);
    }

    @Transactional
    public void deleteTimeSheetById(int userId) {
        List<TimeSheet> timeSheets = timesheetRepo.findByUserid(userId);
        for (TimeSheet ts :  timeSheets) {
            for (String d : ts.getDays()) {
                dayRepo.deleteById(d);
            }
            timesheetRepo.deleteByUserid(userId);
        }
    }


    @Transactional(readOnly=true)
    public TimeSheetDomain getTimeSheetDomain(String weekEnd, int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("before dao");
        TimeSheet ts = timesheetRepo.findByWeekEndAndUserid(weekEnd, userid);

        if (ts != null) {
            return timesheetToDomain(ts);
        }

        throw new RuntimeException("No timesheet found!");
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
        //System.out.println("enter here!");
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
        System.out.println(daysid);
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


    @Transactional
    public void saveTimeSheet(MultipartFile file, TimeSheetDomain tsd){
        String path = tsd.getFilePath();
        if (file != null) path = s3Service.uploadFile(file);

        Optional<TimeSheet> tsOP = Optional.ofNullable(timesheetRepo.findByWeekEndAndUserid(tsd.getWeekEnd(), tsd.getUserid()));


        if (tsOP.isPresent()) {
            System.out.println("Enter tsop");
            TimeSheet ts = tsOP.get();
            System.out.println(ts);
            //update each day
            double count = 0;
            for (int i = 0; i < ts.getDays().size(); i++) {
                String did = ts.getDays().get(i);

                Optional<Day> dayOptional = dayRepo.findById(did);

                if (dayOptional.isPresent()) {

                   // System.out.println("Enter day");
                    Day day = dayOptional.get();
                    day.setDay(tsd.getDays().get(i).getDay());
                    day.setDate(tsd.getDays().get(i).getDate());
                    day.setStartTime(tsd.getDays().get(i).getStartTime());
                    day.setEndTime(tsd.getDays().get(i).getEndTime());
                    day.setIsVacation(tsd.getDays().get(i).getIsVacation());
                    day.setIsHoliday(tsd.getDays().get(i).getIsHoliday());
                    day.setIsFloating(tsd.getDays().get(i).getIsFloating());

                    dayRepo.save(day);
                    count += getTotalBillingHours(tsd.getDays().get(i).getStartTime(), tsd.getDays().get(i).getEndTime()
                            , tsd.getDays().get(i).getDay());


                }
            }
            System.out.println(count);
            ts.setFilePath(path);
            ts.setTotalBillingHours(count);
            timesheetRepo.save(ts);
        }
    }

    double getTotalBillingHours(String sds, String tds, String day) {
        if ( !day.equals("Sunday") && !day.equals("Saturday") && !sds.equals("N/A") && !tds.equals("N/A")) {
            int sd = getMinute(sds);
            int ed = getMinute(tds);
            double bhour = (ed - sd) / 60.0;
            return Math.round(bhour * 100.0) / 100.0;
        }
        else return 0;
    }

    public int getMinute(String timeValue){

        String[] splitByColon = timeValue.split(":");
        int hoursValue = Integer.parseInt(splitByColon[0]);

        String[] splitForMins = splitByColon[1].split(" ");

        if(splitForMins[1].equals("P.M."))
        {
            hoursValue = hoursValue + 12;
        }

        int minutesValue = Integer.parseInt(splitForMins[0]);

        return 60*hoursValue + minutesValue;

    }


    @Transactional
    public void setDefault(TimeSheetDomain tsd){
        Optional<TimeSheet> tsOP = Optional.ofNullable(timesheetRepo.findByWeekEndAndUserid("00/00/0000", tsd.getUserid()));


        if (tsOP.isPresent()) {
            TimeSheet ts = tsOP.get();
            System.out.println(ts);
            //update each day
            int count = 0;
            for (int i = 0; i < ts.getDays().size(); i++) {
                String did = ts.getDays().get(i);

                Optional<Day> dayOptional = dayRepo.findById(did);

                if (dayOptional.isPresent()) {
                    Day day = dayOptional.get();
                    day.setDay(tsd.getDays().get(i).getDay());
                    day.setDate(tsd.getDays().get(i).getDate());
                    day.setStartTime(tsd.getDays().get(i).getStartTime());
                    day.setEndTime(tsd.getDays().get(i).getEndTime());
                    day.setIsVacation(tsd.getDays().get(i).getIsVacation());
                    day.setIsHoliday(tsd.getDays().get(i).getIsHoliday());
                    day.setIsFloating(tsd.getDays().get(i).getIsFloating());

                    dayRepo.save(day);

                    int sd = parseInt(tsd.getDays().get(i).getStartTime().replaceAll("\\D+",""));
                    int ed = parseInt(tsd.getDays().get(i).getEndTime().replaceAll("\\D+",""));

                    count += ed < sd ? ((ed + 12 -sd) > 8 ? 8 : (ed + 12 -sd)): ed-sd;
                }
            }

            ts.setFilePath(tsd.getFilePath());
            ts.setTotalBillingHours(count);
            timesheetRepo.save(ts);
        }
        else {
            tsd.setWeekEnd("00/00/0000");

        }



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
                .filePath(ts.getFilePath())
                .weekEnd(ts.getWeekEnd())
                .build();
    }




}
