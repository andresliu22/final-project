package com.example.timesheetserver;

import com.example.timesheetserver.dao.SummaryRepo;
import com.example.timesheetserver.entity.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableScheduling
public class TimeSheetServerApplication {

    @Autowired
    private SummaryRepo smRepo;

    public static void main(String[] args) {


        SpringApplication.run(TimeSheetServerApplication.class, args);
        TimeSheetServerApplication ta = new TimeSheetServerApplication();
        ta.scheduleAddSummary();
    }

    @Scheduled(fixedRate = 1000, initialDelay = 1000)
    public void scheduleAddSummary() {
        if (smRepo == null) {
            return;
        }
        if (smRepo.findTopByOrderByWeekEndingDesc() == null) {
            List<String> dates = collectLocalDates(LocalDate.now().minusMonths(2), LocalDate.now());
            for (String s : dates) {
                LocalDate d = LocalDate.parse(s);
                if (DayOfWeek.of(d.get(ChronoField.DAY_OF_WEEK)) == DayOfWeek.SATURDAY) {

                    addSummary(s);
                }
            }
        }


        String weekE = smRepo.findTopByOrderByWeekEndingDesc().getWeekEnding().replaceAll("/", "-");
        String[] format = weekE.split("-");
        StringBuilder sb = new StringBuilder();
        sb.append(format[2] + "-");
        sb.append(format[0] + "-");
        sb.append(format[1]);
        weekE = sb.toString();

        LocalDate weekEnding = LocalDate.parse(weekE);
        LocalDate cur = LocalDate.now();
        if (weekEnding.isBefore(cur)) {
            LocalDate d = weekEnding.plusWeeks(1);
            addSummary(d.toString());
        }

    }


    public void addSummary(String date) {
        Summary sm = new Summary();
        String[] format = date.split("-");
        StringBuilder sb = new StringBuilder();
        sb.append(format[1] + "/");
        sb.append(format[2] + "/");
        sb.append(format[0]);
        date = sb.toString();

        sm.setWeekEnding(date);
        sm.setOption("edit");
        sm.setComment("");
        sm.setTotalHours(40.0);
        sm.setApprovalStatues("N/A");
        sm.setSubmissionStatus("Incomplete");
        smRepo.insert(sm);
    }

    public static List<String> collectLocalDates(LocalDate start, LocalDate end) {

        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }
}