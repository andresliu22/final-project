package com.example.timesheetserver.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.timesheetserver.entity.Day;

public interface DayRepo extends MongoRepository<Day, String> {
}
