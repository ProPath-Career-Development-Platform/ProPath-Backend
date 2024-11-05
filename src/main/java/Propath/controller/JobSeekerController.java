package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.dto.JobDto;
import Propath.dto.JobSeekerDto;
import Propath.model.Company;
import Propath.model.Job;
import Propath.service.CompanyService;
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
    private JobSeekerService jobSeekerService;



    @GetMapping("getCompany")
    public List<JobDto> getAllCompanies(){
        return jobService.getAllJobs();
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

}
