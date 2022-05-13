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
import com.example.timesheetserver.exception.saveException;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.timesheetserver.service.AmazonS3Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import static java.lang.Integer.parseInt;
@Log4j2
@Service
public class TimeSheetService {

    final static int MAX_FLOATING = 3;
    final static int MAX_VACATION = 3;
    @Autowired
    private SummaryRepo smRepo;

//    JSONParser parser = new JSONParser();

    @Autowired
    private AsyncTimeSheetService asyncTimeSheetService;



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

//    List<String> getDates(String filepath) {
//        List<String> dates = new ArrayList<>();
//
//        JSONParser parser = new JSONParser();
//        try {
//            Object obj = parser.parse(new FileReader("TimeSheetServer/src/main/java/com/example/timesheetserver/resource/holidays.json"));
//
//            JSONObject jsonObject =  (JSONObject) obj;
//            Object obj1 = (JSONObject) jsonObject.get("response");
//            JSONObject jobj2 = (JSONObject) obj1;
//            JSONArray a = (JSONArray) jobj2.get("holidays");
//            for (Object o : a) {
//                JSONObject obj3 = (JSONObject) o;
//                Object obj4 = obj3.get("date");
//                JSONObject jobj4 = (JSONObject) obj4;
//                dates.add((String) jobj4.get("iso"));
//            }
//
//            //System.out.println(dates);
//            return dates;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return dates;
//    }


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
    @Cacheable(cacheNames="showmore")
    public List<SummaryDomain> ShowMore(List<SummaryDomain> ls,int userid){


        LocalDate date= weekEndingToLocaldate(ls.get(ls.size()-1).getWeekEnding());
        LocalDate cDate=date.minusWeeks(6);
        Summary summary=new Summary();

        log.warn("Async executing: "+ Thread.currentThread().getName());

        List<SummaryDomain> res = smRepo.findAllByOrderByWeekEndingDesc().stream().filter((s)->weekEndingToLocaldate(s.getWeekEnding()).isAfter(cDate))
                .map(p-> summaryToDomain(p)).collect(Collectors.toList());
        CompletableFuture<SummaryDomain>[] csm=res.stream().map(sm->asyncTimeSheetService.show(sm,userid)).toArray(CompletableFuture[]::new);

        CompletableFuture<List<SummaryDomain>> comsm = CompletableFuture
                .allOf(csm)
                .thenApply(v->{
                    return Arrays.stream(csm)
                            .map(pf->pf.join())
                            .filter(s->s!=null) //
                            .collect(Collectors.toList());
                });
        try {

            return comsm.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Transactional
    public TimeSheetDomain view(String weekEnding,int userid){
        TimeSheet res=timesheetRepo.findByWeekEndAndUserid(weekEnding,userid);
        return timesheetToDomain(res);
    }


    @Transactional
    public TimeSheetDomain edit(String weekEnding,int userid){
//        getDates("TimeSheetServer/src/main/java/com/example/timesheetserver/resource/holidays.json");
        TimeSheet ts=timesheetRepo.findByWeekEndAndUserid(weekEnding,userid);
        Summary smd=smRepo.findByWeekEnding(weekEnding);

        if(ts!=null){
            return timesheetToDomain(ts);
        }
        else{

            TimeSheet defau=timesheetRepo.findByWeekEndAndUserid("00/00/0000",userid);
            if(defau!=null){
                TimeSheetDomain tsd = TimeSheetDomain.builder().totalBillingHours(defau.getTotalBillingHours()).
                        approvalStatus(defau.getApprovalStatus())
                        .totalCompensatedHours(defau.getTotalCompensatedHours())
                        .days(createTimeSheet(summaryToDomain(smd), userid).getDays()).
                        floatingDaysWeek(defau.getFloatingDaysWeek()).
                        weekEnd(weekEnding)
                        .userid(defau.getUserid()).build();

                createtimeSheet(tsd);
                return tsd;
            }
            else{
                TimeSheetDomain tsd = createTimeSheet(summaryToDomain(smd),userid);
                createtimeSheet(tsd);
                return tsd;

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


    public TimeSheetDomain createTimeSheet(SummaryDomain sm,int userid){
//        List<String> d = getDates("TimeSheetServer/src/main/java/com/example/timesheetserver/resource/holidays.json");

        Set<String> dates = new HashSet<String>(Arrays.asList("2022-01-01", "2022-11-24", "2022-12-25", "2022-07-04","2022-09-05", "2022-04-17","2022-04-25","2022-05-01"));
        System.out.println(dates);



        String weekEnding=sm.getWeekEnding();
        List<DayDomain> days=new ArrayList<>();
        String[] weekday={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        LocalDate date=weekEndingToLocaldate(weekEnding);


        int count = 0;
        for(int i=0;i<7;i++){
            String startingTime="9:00 A.M.";
            String endingTime="6:00 P.M.";
            if(i==0||i==6){
                startingTime="N/A";
                endingTime="N/A";
            }

            String dstring = date.minusDays(6).plusDays(i).toString();
            boolean isholiday = false;

            if (dates.contains(dstring)) {
                startingTime="N/A";
                endingTime="N/A";
                isholiday = true;
                if ( i!=0 && i!=6) count++;
            }

            DayDomain dd=DayDomain.builder().day(weekday[i]).date(localdateToWeekEnding(date.minusDays(6).plusDays(i)))
                    .endTime(endingTime)
                    .startTime(startingTime).isFloating(false).isHoliday(isholiday).isVacation(false).build();
            days.add(dd);
        }
        TimeSheetDomain tsd=TimeSheetDomain.builder().
                userid(userid).
                totalCompensatedHours(sm.getTotalHours()).
                totalBillingHours(sm.getTotalHours() - count * 8).
                weekEnd(weekEnding).
                days(days).
                filePath("").
                floatingDaysWeek(0).
                vocationDaysWeek(0).
                approvalStatus("Not approved").
                submissionStatus("Incomplete").build();
        return tsd;
    }
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
    public void createtimeSheet(TimeSheetDomain timesheetDomain) {
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
        //System.out.println(daysid);
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
        ts.setFilePath("");

        timesheetRepo.insert(ts);
    }


    @Transactional
    public void saveTimeSheet(MultipartFile file, TimeSheetDomain tsd, String userid) throws saveException{
        System.out.println(tsd.getWeekEnd());
        String path = tsd.getFilePath();
        if (file != null) path = s3Service.uploadFile(file);
//        System.out.println(path);

//        File file = new File("file:///F:/beaconfire/assignments/Week%201%20Day%203%20-%20Xianzhi%20Luo/short%20answer.pdf");


        List<TimeSheet> timeSheets = timesheetRepo.findByUserid(Integer.parseInt(userid));
        int floatingCount = 0;
        int vacationCount = 0;

        for (TimeSheet ts : timeSheets) {
            for (int i = 0; i < ts.getDays().size(); i++){
                String did = ts.getDays().get(i);

                Optional<Day> dayOptional = dayRepo.findById(did);

                if (dayOptional.isPresent()) {
                    Day day = dayOptional.get();
                    if (day.getIsFloating()) floatingCount++;
                    if ( day.getIsVacation()) vacationCount++;
                }
            }
        }
        System.out.println(floatingCount);
        System.out.println(vacationCount);


        Optional<TimeSheet> tsOP = Optional.ofNullable(timesheetRepo.findByWeekEndAndUserid(tsd.getWeekEnd(), tsd.getUserid()));
        if (tsOP.isPresent()) {
            //System.out.println("Enter tsop");
            TimeSheet ts = tsOP.get();
            //System.out.println(ts);
            //update each day
            double count = 0;
            int fc = 0;
            int vc = 0;
            int hc = 0;





            floatingCount -= ts.getFloatingDaysWeek();
            vacationCount -= ts.getVocationDaysWeek();

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
                    int flag = 0;
                    if (day.getIsVacation()) {
                        flag++;
                        vacationCount++;
                        vc++;
                    }

                    if (day.getIsFloating()) {
                        flag++;
                        floatingCount++;
                        fc++;
                    }
                    if (day.getIsHoliday()) {
                        flag++;
                        if (!day.getDay().equals("Saturday")  && !day.getDay().equals("Sunday")) hc++;
                    }

                    try {
                        if (flag > 1) {
                            throw new saveException("You cannot choose 2 or more!");
                        }
                        else if (flag == 1) {
                            day.setStartTime("N/A");
                            day.setEndTime(("N/A"));
                        }
                        try {
                            if (floatingCount > MAX_FLOATING || vacationCount > MAX_VACATION) {
                                throw new saveException("Floating/ Vacation used up!");
                            }
                        }
                        catch (saveException e) {
                            throw new saveException(e.getMessage());
                        }
                    }
                    catch (saveException e){
                        throw new saveException(e.getMessage());
                    }


                    dayRepo.save(day);
                    if (getTotalBillingHours(day.getStartTime(), day.getEndTime()
                            , tsd.getDays().get(i).getDay()) >= 1) count += getTotalBillingHours(day.getStartTime(), day.getEndTime()
                            , tsd.getDays().get(i).getDay()) - 1;


                }
            }
            System.out.println((double)Math.round(count*100)/100);
            ts.setFilePath(path);
            ts.setTotalBillingHours((double)Math.round(count*100)/100);
            ts.setTotalCompensatedHours((double)Math.round((count + fc * 8.0 + hc * 8.0)*100)/100);
            ts.setVocationDaysWeek(vc);
            ts.setFloatingDaysWeek(fc);
            timesheetRepo.save(ts);
        }
        else{
            TimeSheet ts = new TimeSheet();
            //System.out.println(ts);
            //update each day
            double count = 0;
            int fc = 0;
            int vc = 0;
            int hc = 0;

            for (int i = 0; i < tsd.getDays().size(); i++) {
                DayDomain dd = tsd.getDays().get(i);



                //String day = ts.getDays().get(i);

                // System.out.println("Enter day");


                int flag = 0;
                if (dd.getIsVacation()) {
                    flag++;
                    vacationCount++;
                    vc++;
                }

                if (dd.getIsFloating()) {
                    flag++;
                    floatingCount++;
                    fc++;
                }
                if (dd.getIsHoliday()) {
                    flag++;
                    if (!dd.getDay().equals("Saturday")  && !dd.getDay().equals("Sunday")) hc++;
                }

                try {
                    if (flag > 1) {
                        throw new saveException("You cannot choose 2 or more!");
                    }
                    else if (flag == 1) {
                        dd.setStartTime("N/A");
                        dd.setEndTime(("N/A"));
                    }
                    else if (floatingCount > 3 || vacationCount > 3) {
                        throw  new saveException(("Your floating/vacation days have been used up"));
                    }
                }
                catch (saveException e){
                    throw new saveException(e.getMessage());
                }



                if (getTotalBillingHours(dd.getStartTime(), dd.getEndTime()
                        , dd.getDay()) >= 1) count += getTotalBillingHours(dd.getStartTime(), dd.getEndTime()
                        , dd.getDay()) - 1;

            }
            List<String> daysid = tsd.getDays().stream().map(d->{
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

            ts.setUserid(tsd.getUserid());
            ts.setTotalBillingHours((double)Math.round(count*100)/100);
            ts.setTotalCompensatedHours((double)Math.round((count + fc * 8.0 + hc * 8.0)*100)/100);
            ts.setDays(daysid);
            ts.setApprovalStatus(tsd.getApprovalStatus());
            ts.setSubmissionStatus(tsd.getSubmissionStatus());
            ts.setWeekEnd("00/00/0000");
            ts.setFloatingDaysWeek(fc);
            ts.setVocationDaysWeek(vc);
            ts.setFilePath(tsd.getFilePath());
            //System.out.println((double)Math.round(count*100)/100);

            timesheetRepo.insert(ts);
        }

    }

    double getTotalBillingHours(String sds, String tds, String day) {
        if ( !day.equals("Sunday") && !day.equals("Saturday") && !sds.equals("N/A") && !tds.equals("N/A")) {
            int sd = getMinute(sds);
            int ed = getMinute(tds);
            double bhour = (ed - sd) / 60.0;
            //System.out.println(bhour);
            return bhour;
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
    public void setDefault(TimeSheetDomain tsd, String userid) throws  saveException{
        //Optional<TimeSheet> tsOP = Optional.ofNullable(timesheetRepo.findByWeekEndAndUserid("00/00/0000", tsd.getUserid()));

        tsd.setWeekEnd("00/00/0000");
        saveTimeSheet(null, tsd, userid);
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
                            .startTime(roundHour(d.getStartTime()))
                            .endTime(roundHour(d.getEndTime()))
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


    public String roundHour(String timeValue){

        if ( timeValue.equals("N/A") && timeValue.equals("N/A")) {
            return ("N/A");
        }

        String[] splitByColon = timeValue.split(":");
        int hoursValue = Integer.parseInt(splitByColon[0]);

        String[] splitForMins = splitByColon[1].split(" ");

        if(splitForMins[1].equals("P.M."))
        {
            hoursValue = hoursValue + 12;
        }

        int minutesValue = Integer.parseInt(splitForMins[0]);

        if (minutesValue < 30) {
            return splitByColon[0] + ":" + "00" + " " + splitForMins[1];
        }
        else return Integer.toString(Integer.parseInt(splitByColon[0]) + 1) + ":" + "00" + " " + splitForMins[1];

    }

}