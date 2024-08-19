package Propath.controller;

import Propath.dto.CompanyAndJobsDto;
import Propath.dto.CompanyDto;
import Propath.dto.PostJobDto;
import Propath.service.CompanyService;
import Propath.service.JobPostService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobPostService jobPostService;

    //Create RegisterCompany REST API
    @PostMapping("/Setup")
    public ResponseEntity<CompanyDto> RegisterCompany(@RequestBody CompanyDto companyDto) {
        CompanyDto newCompany = companyService.RegisterCompany(companyDto);
        return new ResponseEntity<>(newCompany, HttpStatus.CREATED);
    }

    @GetMapping("/home")
    public ResponseEntity<CompanyAndJobsDto> getCompanyAndPostedJobs(@RequestParam int userId) {
        // Fetch company details
        CompanyDto company = companyService.getCompanyByUserId(userId);

        // Fetch posted jobs

        List<PostJobDto> postedJobs = jobPostService.getPostedJobs(userId);

        // Create and return composite DTO
        CompanyAndJobsDto companyAndJobs = new CompanyAndJobsDto(company, postedJobs);
        return new ResponseEntity<>(companyAndJobs, HttpStatus.OK);
    }


}

