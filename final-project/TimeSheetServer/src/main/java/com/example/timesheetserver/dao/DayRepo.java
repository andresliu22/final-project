package com.example.timesheetserver.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.timesheetserver.entity.Day;

import java.util.Optional;

public interface DayRepo extends MongoRepository<Day, String> {

    void deleteById(String Id);

    Optional<Day> findById(String id);
}
