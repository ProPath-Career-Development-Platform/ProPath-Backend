package Propath.controller;

import Propath.dto.CompanyAndJobsDto;
import Propath.dto.CompanyDto;
import Propath.dto.JobDto;
import Propath.model.Job;
import Propath.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    // Get related jobs by tags REST API
    @GetMapping("/related-jobs")
    public ResponseEntity<List<CompanyAndJobsDto>> getRelatedJobs(@RequestParam List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        String[] lowercaseTags = tags.stream().map(String::toLowerCase).toArray(String[]::new);
        try {
            List<CompanyAndJobsDto> relatedJobs = jobPostService.findRelatedJobsWithCompanyByTags(lowercaseTags);
            return ResponseEntity.ok(relatedJobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
