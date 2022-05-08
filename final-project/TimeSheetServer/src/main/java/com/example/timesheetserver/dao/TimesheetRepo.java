package com.example.timesheetserver.dao;

import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TimesheetRepo extends MongoRepository<TimeSheet, String> {


    void deleteByUseridAndWeekEnd(int userid, String weekEnd);

    void deleteByUserid(int userid);

    // https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html

//    Optional<TimeSheet> findFirst5ByWeekEnd(String weekEnd, Sort sort);
//
    TimeSheet findByWeekEndAndUserid(String weekEnd, int userid);

    List<TimeSheet> findByUserid(int userId);


//    @Query(value="{'name': {'$ne': ?0} }")
//    List<TimeSheet> findByNameNot(String name);
}