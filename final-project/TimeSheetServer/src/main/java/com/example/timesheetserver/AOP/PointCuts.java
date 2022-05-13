package com.example.timesheetserver.AOP;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

// Spring Team suggests defining all point cuts in one class
@Aspect
public class PointCuts {


    /*
    Another way to achieve the same result from the previous section is by using the within PCD,
    which limits matching to join points of certain types.
     */
    @Pointcut("within(com.example.timesheetserver.service.TimeSheetService)")
    public void allmethod() {}


}
