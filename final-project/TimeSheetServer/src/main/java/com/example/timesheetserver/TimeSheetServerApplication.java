package com.example.timesheetserver;

import com.example.timesheetserver.dao.SummaryRepo;
import com.example.timesheetserver.domain.HolidayDomain;
import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.entity.Summary;
import com.github.agogs.holidayapi.api.APIConsumer;
import com.github.agogs.holidayapi.api.impl.HolidayAPIConsumer;
import com.github.agogs.holidayapi.model.Holiday;
import com.github.agogs.holidayapi.model.HolidayAPIResponse;
import com.github.agogs.holidayapi.model.QueryParams;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
@EnableAsync
@EnableCaching
public class TimeSheetServerApplication {

    @Autowired
    private SummaryRepo smRepo;
    public static void main(String[] args) {


        SpringApplication.run(TimeSheetServerApplication.class, args);
//        TimeSheetServerApplication ta=new TimeSheetServerApplication();
//        ta.scheduleAddSummary();
    }

    @Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("showmore"),new ConcurrentMapCache("loaduser")));
        return cacheManager;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.timesheetserver.controller"))
                .build();
    }

     @Scheduled(fixedRate=1000,initialDelay = 1000)
    public void scheduleAddSummary(){

        if(smRepo==null){
            return;
        }
        if(smRepo.findTopByOrderByWeekEndingDesc()==null){
            List<String> dates=collectLocalDates(LocalDate.now().minusMonths(2),LocalDate.now());
            for(String s:dates){
                LocalDate d=LocalDate.parse(s);
                if(DayOfWeek.of(d.get(ChronoField.DAY_OF_WEEK))==DayOfWeek.SATURDAY){

                    addSummary(s);
                }
            }
        }


        String weekE=smRepo.findTopByOrderByWeekEndingDesc().getWeekEnding().replaceAll("/","-");
        String[] format=weekE.split("-");
        StringBuilder sb=new StringBuilder();
        sb.append(format[2]+"-");
        sb.append(format[0]+"-");
        sb.append(format[1]);
        weekE=sb.toString();

        LocalDate weekEnding=LocalDate.parse(weekE);
        LocalDate cur=LocalDate.now();
        if(weekEnding.isBefore(cur)){
            LocalDate d=weekEnding.plusWeeks(1);
            addSummary(d.toString());
        }


    }


    public void addSummary(String date){
        Summary sm=new Summary();
        String[] format=date.split("-");
        StringBuilder sb=new StringBuilder();
        sb.append(format[1]+"/");
        sb.append(format[2]+"/");
        sb.append(format[0]);
        date=sb.toString();

        sm.setWeekEnding(date);
        sm.setOption("edit");
        sm.setComment("");
        sm.setTotalHours(40);
        sm.setApprovalStatues("N/A");
        sm.setSubmissionStatus("Not Started");
        smRepo.insert(sm);
    }

    public static List<String> collectLocalDates(LocalDate start, LocalDate end){

        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }


    public void getHolidy(){
        APIConsumer consumer = new HolidayAPIConsumer("https://holidayapi.com/v1/holidays");

        //generate the wuery parameters
        QueryParams params = new QueryParams();
        params.key("b3c0a57a-2823-4d07-9aa1-d2ba98b875aa")
                .month(1)
                .country(QueryParams.Country.INDIA)
                .year(2022)
                //JSON is the default format
                .format(QueryParams.Format.XML)
                .pretty(true);

        try {
            //make the API call
            HolidayAPIResponse response = consumer.getHolidays(params);

            //check the status code of the API call
            int status = response.getStatus();
            if (status != 200) {
                System.out.println(200);

                //handle error scenario

            } else {

                //handle success scenario

                List<Holiday> holidays = response.getHolidays();
                for (Holiday h : holidays) {
                    //do your thing
//                    Gson gson=new Gson();
//                    HolidayDomain hd=gson.fromJson(h);
                    System.out.println(h);
                }
            }
        } catch (IOException e) {
            System.out.println("exception:"+e);
            //handle exception
        }
    }

}
