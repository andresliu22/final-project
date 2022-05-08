package com.example.timesheetserver.service;

import com.example.timesheetserver.dao.DayRepo;
import com.example.timesheetserver.dao.TimesheetRepo;
import com.example.timesheetserver.domain.DayDomain;
import com.example.timesheetserver.domain.TimeSheetDomain;
import com.example.timesheetserver.domain.DayDomain;
import com.example.timesheetserver.entity.Day;
import com.example.timesheetserver.entity.TimeSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private DayRepo dayRepo;

    @Autowired
    private TimesheetRepo timesheetRepo;

    /*@Transactional(readOnly=true)
    public List<SummaryDomain> getSummary(int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" name");
        Optional<Product> productOptional = productRepo.findByName(name);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return List<SummaryDomain>;
        }

        throw new RuntimeException("No product found");
    }*/

    /*@Transactional(readOnly=true)
    public List<SummarYDomain> getFiveMore( int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" name");
        Optional<Product> productOptional = productRepo.findByName(name);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return List<SummaryDomain>;
        }

        throw new RuntimeException("No product found");
    }*/





    @Transactional(readOnly=true)
    public TimeSheetDomain getTimeSheetDomain(String weekEnd, int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("before dao");
        TimeSheet ts = timesheetRepo.findtimeSheetsBy(weekEnd, userid);

        if (ts != null) {
            return timesheetToDomain(ts);
        }

        throw new RuntimeException("No product found");
    }

    /*@Transactional(readOnly=true)
    public List<dayDomain> getDays(String weekEnd, int userid) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name+" name");
        Optional<Product> productOptional = productRepo.findByName(name);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return List<SummaryDomain>;
        }

        throw new RuntimeException("No product found");
    }*/



    /*@Transactional
    public void createTimeSheet(timeSheetDomain timesheetDomain) {
        List<String> newIngredientIds = productDomain.getIngredients().stream().map(i->{
                Ingredient ingredient = new Ingredient();
                ingredient.setName(i.getName());
                ingredient.setType(i.getType());
                ingredient.setIsVerified(i.getIsVerified());

                Ingredient newIngredient = ingredientRepo.insert(ingredient);
                return newIngredient.getId();
            }).collect(Collectors.toList());

        Product newProduct = new Product();
        newProduct.setName(productDomain.getName());
        newProduct.setPrice(productDomain.getPrice());
        newProduct.setPopularity(productDomain.getPopularity());
        newProduct.setIngredients(newIngredientIds);

        productRepo.insert(newProduct);
    }
*/





    // Convert timesheet entity  to domain
    TimeSheetDomain timesheetToDomain(TimeSheet ts) {
        List<String> days = ts.getDays();

        List<DayDomain> dayDomains = days.stream()
                .map(id->dayRepo.findById(id))
                .filter(o->o.isPresent())
                .map(o->{
                    Day d = o.get();
                    return DayDomain.builder()
                            .date(d.getDate())
                            .day(d.getDay())
                            .isFloating(d.getIsFloating())
                            .isHoliday(d.getIsHoliday())
                            .isVacation(d.getIsVacation())
                            .startTime(d.getStartTime())
                            .endTime(d.getEndTime())
                            .build();
                }).collect(Collectors.toList());

        return TimeSheetDomain.builder()
                .userid(ts.getUserid())
                .days(dayDomains)
                .totalBillingHours(ts.getTotalBillingHours())
                .totalCompensatedHours(ts.getTotalCompensatedHours())
                .approvalStatus(ts.getApprovalStatus())
                .submissionStatus(ts.getSubmissionStatus())
                .floatingDaysWeek(ts.getFloatingDaysWeek())
                .vocationDaysWeek(ts.getVocationDaysWeek())
                .build();
    }




}
