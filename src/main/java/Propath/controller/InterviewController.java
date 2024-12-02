package Propath.controller;

import Propath.dto.InterviewDto;
import Propath.model.JobSeeker;
import Propath.service.ApplicantService;
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
    private final ApplicantService applicantService;

    @Autowired
    public InterviewController(InterviewService interviewService, JobService jobService,ApplicantService applicantService) {
        this.interviewService = interviewService;
        this.jobService = jobService;
        this.applicantService = applicantService;
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


    @PutMapping("interviews/updateStatusToHired")
    public ResponseEntity<String> updateStatusToHired(@RequestParam Long interviewId, @RequestParam Integer userId, @RequestParam Long jobId) {
        try {
            // Update interview status to 'CONDUCTED'
            interviewService.updateInterviewStatus(interviewId, "CONDUCTED");

            // Update applicant status to 'HIRED'
            applicantService.updateApplicantStatus(userId, jobId, "HIRED");


            return ResponseEntity.ok("Interview status updated to 'CONDUCTED' and Applicant status updated to 'HIRED'");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating interview and applicant status: " + e.getMessage());
        }
    }

    @PutMapping("interviews/updateStatusToReject")
    public ResponseEntity<String> updateStatusToReject(@RequestParam Long interviewId, @RequestParam Integer userId, @RequestParam Long jobId) {
        try {
            // Update interview status to 'CONDUCTED'
            interviewService.updateInterviewStatus(interviewId, "CONDUCTED");

            // Update applicant status to 'REJECTED'
            applicantService.updateApplicantStatus(userId, jobId, "REJECTED");

            return ResponseEntity.ok("Interview status updated to 'CONDUCTED' and Applicant status updated to 'REJECTED'");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating interview and applicant status: " + e.getMessage());
        }
    }
}
