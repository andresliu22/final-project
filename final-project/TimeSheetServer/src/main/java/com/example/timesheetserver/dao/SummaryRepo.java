package com.example.timesheetserver.dao;

import com.example.timesheetserver.domain.SummaryDomain;
import com.example.timesheetserver.entity.Summary;
import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SummaryRepo extends MongoRepository<Summary, Integer> {
    List<Summary> findTop5ByOrderByWeekEndingDesc();
    Summary findTopByOrderByWeekEndingDesc();

    List<Summary> findAllByOrderByWeekEndingDesc();

}

