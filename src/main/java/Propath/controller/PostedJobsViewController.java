package Propath.controller;

import Propath.dto.PostJobDto;
import Propath.service.JobPostService;
import jakarta.servlet.http.HttpServletRequest;
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
    private JobPostService jobPostService;

    //Build get all jobs REST API
    @GetMapping("/all-jobs")
    public ResponseEntity<List<PostJobDto>> getAllPostJobs() {
        List<PostJobDto> postJobDtos = jobPostService.getAllPostJobs();
        return new ResponseEntity<>(postJobDtos, HttpStatus.OK);
    }

    //get image
}
