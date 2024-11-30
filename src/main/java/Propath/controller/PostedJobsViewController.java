package Propath.controller;

import Propath.dto.*;
import Propath.model.Job;
import Propath.service.*;
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

    @Autowired
    private PdfService pdfService;

    @Autowired
    private TextMatchingService textMatchingService;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private InterviewService interviewService;

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
    @GetMapping("/related-jobs/{jobId}")
    public ResponseEntity<List<JobDto>> getRelatedJobs(@PathVariable Long jobId) {
        try {
            List<JobDto> relatedJobs = jobPostService.getRelatedJobsByTags(jobId);
            return ResponseEntity.ok(relatedJobs);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    // save favorite job REST API
//    @PostMapping("/favorite-job")
//    public ResponseEntity<?> saveFavoriteJob(@RequestBody FavoriteJobsDto favoriteJobsDto) {
//        try {
//            FavoriteJobsDto savedJob = favoriteJobs.saveFavoriteJob(favoriteJobsDto);
//            return ResponseEntity.ok(savedJob);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while saving the favorite job");
//        }
//    }

    // apply for job REST API
    @PostMapping("/apply")
    public ResponseEntity <ApplicantDto> saveApplication(@RequestBody ApplicantDto applicantDto){

        String cvText = pdfService.extractTextFromPdfUrl(applicantDto.getCv());
        applicantDto.setCvText(cvText);

        Job job = applicantDto.getJob();
        String jobD = job.getJobDescription();

        double atsScore = textMatchingService.calculateMatchPercentage(jobD,cvText);

        applicantDto.setAtsScore((int)atsScore);
        ApplicantDto savedAppliation = applicantService.saveApplication(applicantDto);

        return new ResponseEntity<>(savedAppliation,HttpStatus.OK);
    }

    // check user already applied for job REST API
    @GetMapping("/check-applied/{userId}/{jobId}")
    public ResponseEntity<Boolean> checkUserAlreadyApplied(@PathVariable Integer userId, @PathVariable Long jobId) {
        Boolean isApplied = applicantService.checkUserAlreadyApplied(userId, jobId);
        return new ResponseEntity<>(isApplied, HttpStatus.OK);
    }

    // get applied jobs by user id REST API
    @GetMapping("/applied-jobs/{userId}")
    public ResponseEntity<List<ApplicantDto>> getAppliedJobsByUserId(@PathVariable Integer userId) {
        List<ApplicantDto> appliedJobs = applicantService.getAppliedJobsByUserId(userId);
        return new ResponseEntity<>(appliedJobs, HttpStatus.OK);
    }

    // get selected or pre-selected applicants REST API
    @GetMapping("/selected-applicants")
    public ResponseEntity<List<ApplicantDto>> getSelectedOrPreSelectedApplicants() {
        List<ApplicantDto> applicants = applicantService.getSelectedOrPreSelectedApplicants();
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    // get interview details by job id REST API
    @GetMapping("/interviews/{jobId}")
    public ResponseEntity<List<InterviewDto>> getInterviewsByJobId(@PathVariable Long jobId) {
        List<InterviewDto> interviews = interviewService.findInterviewsByJobId(jobId);
        return ResponseEntity.ok(interviews);
    }

    // get interviews for selected or pre-selected applicants REST API
    @GetMapping("/selected-preselected-interviews")
    public ResponseEntity<List<InterviewDto>> getInterviewsForSelectedOrPreSelectedApplicants() {
        List<InterviewDto> interviews = applicantService.getInterviewsForSelectedOrPreSelectedApplicants();
        return ResponseEntity.ok(interviews);
    }

    // update interview REST API
    @PutMapping("/update-interview/{interviewId}")
    public ResponseEntity<InterviewDto> updateInterview(@PathVariable Long interviewId, @RequestBody InterviewDto interviewDto) {
        InterviewDto updatedInterview = interviewService.updateInterview(interviewId, interviewDto);
        return ResponseEntity.ok(updatedInterview);
    }


}
