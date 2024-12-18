package Propath.controller;

import Propath.dto.*;
import Propath.model.Company;
import Propath.model.Job;
import Propath.service.CompanyService;
import Propath.service.JobSeekerEventService;
import Propath.service.JobSeekerService;
import Propath.service.JobService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/jobseeker")
public class JobSeekerController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobSeekerEventService jobSeekerEventService;

    @Autowired
    private JobSeekerService jobSeekerService;



    @GetMapping("getCompany")
    public List<JobDto> getAllCompanies(@RequestParam("filter") List<String> filter, @RequestParam String jobType){
        return jobService.getAllJobs(filter,jobType);
    }

    @GetMapping("getJobById")
    public JobDto getJobById(@RequestParam("Id") Long Id){
        JobDto jobDto = jobService.getJobByIdJs(Id);
        return jobDto;
    }

    @GetMapping("getUserDetails")
    public JobSeekerDto getUserDetails(){

        JobSeekerDto jobSeekerDto = jobSeekerService.getJobSeekerDetails();
        return jobSeekerDto;
    }

    @GetMapping("getEventById/{id}")
    public JobSeekerEventDto getEventById(@PathVariable("id") long eventId){
        return jobSeekerEventService.getJobSeekerEventById(eventId);
    }

    @GetMapping("getNotifications/{id}")
    public List<List<String>> getNotifications(@PathVariable("id") long id){
        return jobSeekerService.getNotifications();
    }

    @GetMapping("getAppliedEvents/{id}")
    public List<JobSeekerEventDto> getAppliedEvents(@PathVariable("id") long id){
        return jobSeekerEventService.getEventBySeekerId(id);
    }
}
