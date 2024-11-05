package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.dto.JobDto;
import Propath.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobseeker")
public class PostedJobsViewController {

    @Autowired
    private JobService jobPostService;

    // Get all jobs REST API
    @GetMapping("/all-jobs")
    public ResponseEntity<List<JobDto>> getAllPostJobs() {
        List<JobDto> postJobDtos = jobPostService.getAllPostJobs();
        return new ResponseEntity<>(postJobDtos, HttpStatus.OK);
    }

    // Get job details by job ID REST API
    @GetMapping("/jobDetails/{id}")
    public ResponseEntity<JobDto> getPostJobById(@PathVariable("id") Long postId) {
        JobDto postJobDto = jobPostService.getPostJobById(postId);
        return new ResponseEntity<>(postJobDto, HttpStatus.OK);
    }

    // Get company information by job ID REST API
    @GetMapping("/postedCompany/{id}")
    public ResponseEntity<CompanyDto> getCompanyInfoByJobId(@PathVariable("id") Long jobId) {
        CompanyDto companyDto = jobPostService.getCompanyInfoByJobId(jobId);
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }
}
