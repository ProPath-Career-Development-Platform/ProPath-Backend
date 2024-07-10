package Propath.controller;

import Propath.dto.PostJobDto;
import Propath.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobprovider/post-a-job")
public class JobPostController {
    @Autowired
    private JobPostService jobPostService;

    //Build save jobs REST API
    @PostMapping
    public ResponseEntity<PostJobDto> savePostJob(@RequestBody PostJobDto postJobDto) {
        PostJobDto savedPostJob = jobPostService.savePostJob(postJobDto);
        return new ResponseEntity<>(savedPostJob, HttpStatus.CREATED);
    }
}
