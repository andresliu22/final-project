package com.example.timesheetserver.dao;

import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TimesheetRepo extends MongoRepository<TimeSheet, String> {
    Optional<TimeSheet> findByName(String name);
    void deleteByName(String name);

    // https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html

    Optional<TimeSheet> findFirst5ByWeekEnd(String weekEnd, Sort sort);

    TimeSheet findtimeSheetsBy(String weekEnd, int userid);



    @Query(value="{'name': {'$ne': ?0} }")
    List<TimeSheet> findByNameNot(String name);
}