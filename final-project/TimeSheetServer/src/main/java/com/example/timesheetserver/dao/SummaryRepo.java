package com.example.timesheetserver.dao;

import com.example.timesheetserver.entity.Summary;
import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SummaryRepo extends MongoRepository<TimeSheet, String> {
//    Summary findTop5();
}
