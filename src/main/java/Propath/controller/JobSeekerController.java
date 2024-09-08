package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.dto.JobDto;
import Propath.model.Company;
import Propath.model.Job;
import Propath.service.CompanyService;
import Propath.service.JobService;
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



    @GetMapping("getCompany")
    public List<JobDto> getAllCompanies(){
        List<JobDto> jobs = jobService.getAllJobs();
        return jobs;
    }

    @GetMapping("getJobById")
    public JobDto getJobById(@RequestParam("Id") Long Id){
        JobDto jobDto = jobService.getJobByIdJs(Id);
        return jobDto;
    }

}
