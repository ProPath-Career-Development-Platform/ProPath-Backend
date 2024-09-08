package Propath.controller;

import Propath.dto.InterviewDto;
import Propath.service.InterviewService;
import Propath.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class InterviewController {

    private final InterviewService interviewService;
    private final JobService jobService;

    @Autowired
    public InterviewController(InterviewService interviewService, JobService jobService) {
        this.interviewService = interviewService;
        this.jobService = jobService;
    }



    @PostMapping("/createInterview/{jobId}")
    public ResponseEntity<String> createInterviews(@RequestBody List<InterviewDto> interviewDto, @PathVariable("jobId") Long jobId) {
        interviewService.createInterview(interviewDto, jobId);
        return ResponseEntity.ok("Interviews created successfully");
    }

    @GetMapping("/getInterviews/{userId}")
    public ResponseEntity<List<InterviewDto>> getMyInterviews(@PathVariable("userId") int userId){

        List<Integer> jobIds = jobService.findJobIdsByProviderId(userId);

        List<InterviewDto> interviews = interviewService.findInterviewsByJobIds(jobIds);
        return ResponseEntity.ok(interviews);
    }
}
