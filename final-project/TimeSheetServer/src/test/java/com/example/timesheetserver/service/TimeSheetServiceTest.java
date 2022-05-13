package com.example.timesheetserver.service;


import com.example.timesheetserver.TimeSheetServerApplication;
import com.example.timesheetserver.domain.SummaryDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TimeSheetServerApplication.class)
public class TimeSheetServiceTest {
    @Autowired
    private TimeSheetService timeSheetService;

    private static List<SummaryDomain> list;
//  private Employee employee1;
//  private Address address1;

    static {
        list = new ArrayList<>();
        list.add(SummaryDomain.builder().weekEnding("03/12/2022").totalHours(40.0).submissionStatus("Not Started").approvalStatus("N/A").option("edit").comment("").build());
        list.add(SummaryDomain.builder().weekEnding("03/19/2022").totalHours(40.0).submissionStatus("Not Started").approvalStatus("N/A").option("edit").comment("").build());
        list.add(SummaryDomain.builder().weekEnding("03/26/2022").totalHours(40.0).submissionStatus("Not Started").approvalStatus("N/A").option("edit").comment("").build());
        list.add(SummaryDomain.builder().weekEnding("04/02/2022").totalHours(40.0).submissionStatus("Not Started").approvalStatus("N/A").option("edit").comment("").build());
        list.add(SummaryDomain.builder().weekEnding("04/09/2022").totalHours(40.0).submissionStatus("Not Started").approvalStatus("N/A").option("edit").comment("").build());

    }


    @Test
    public void getAllSummariesTest(){
        assertAll(
                () -> assertEquals(list.stream().filter((e)->e.getWeekEnding().equals("03/12/2022")).collect(Collectors.toList()).get(0).getWeekEnding(), timeSheetService.getAllSummaries().get(0).getWeekEnding()),
                () -> assertEquals(list.stream().filter((e)->e.getWeekEnding().equals("03/19/2022")).collect(Collectors.toList()).get(0).getWeekEnding(), timeSheetService.getAllSummaries().get(1).getWeekEnding()),
                () -> assertEquals(list.stream().filter((e)->e.getWeekEnding().equals("03/26/2022")).collect(Collectors.toList()).get(0).getWeekEnding(), timeSheetService.getAllSummaries().get(2).getWeekEnding()),
                () -> assertEquals(list.stream().filter((e)->e.getWeekEnding().equals("04/02/2022")).collect(Collectors.toList()).get(0).getWeekEnding(), timeSheetService.getAllSummaries().get(3).getWeekEnding()),
                () -> assertEquals(list.stream().filter((e)->e.getWeekEnding().equals("04/09/2022")).collect(Collectors.toList()).get(0).getWeekEnding(), timeSheetService.getAllSummaries().get(4).getWeekEnding())
        );
    }

}