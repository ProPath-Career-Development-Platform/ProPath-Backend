package Propath.controller;

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

    //Build get all jobs REST API
    @GetMapping("/all-jobs")
    public ResponseEntity<List<JobDto>> getAllPostJobs() {
        List<JobDto> postJobDtos = jobPostService.getAllPostJobs();
        return new ResponseEntity<>(postJobDtos, HttpStatus.OK);
    }

    @GetMapping("jobDetails/{id}")
    public ResponseEntity<JobDto> getPostJobById(@PathVariable("id") Long postId) {
        System.out.println("Received job ID: " + postId);
        JobDto postJobDto = jobPostService.getPostJobById(postId);
        return new ResponseEntity<>(postJobDto, HttpStatus.OK);
    }

}
