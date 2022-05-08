package com.example.timesheetserver.dao.Scheduler;

import com.example.timesheetserver.dao.SummaryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class CreateSummary {
    @Autowired
    private SummaryRepo smRepo;



     @Scheduled(fixedRate=10000)
    public void scheduleAddSummary(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String curDate= dateFormat.format(date);
        LocalDate weekEnding=LocalDate.parse(smRepo.findTopByOrderByWeekEndingDesc().getWeekEnding());
        LocalDate cur=LocalDate.parse(curDate);
        System.out.println(weekEnding.toString()+cur.toString());

    }
    public  static  int  dayForWeek(String pTime) throws  Exception {
        DateFormat format = new  SimpleDateFormat("yyyy-MM-dd" );
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int  dayForWeek = 0 ;
        if (c.get(Calendar.DAY_OF_WEEK) == 1 ){
            dayForWeek = 7 ;
        }else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1 ;
        }
        return  dayForWeek;
    }
}
