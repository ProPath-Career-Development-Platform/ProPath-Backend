package Propath.controller;

import Propath.dto.*;
import Propath.model.Job;
import Propath.model.User;
import Propath.repository.UserRepository;
import Propath.service.ApplicantService;
import Propath.service.FavoritesJobsService;
import Propath.service.InterviewService;
import Propath.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ApplicantService applicantService;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private FavoritesJobsService favoritesJobsService;

    @Autowired
    private UserRepository userRepository;

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

    // save Favorite job REST API
    @PostMapping("/save-favorite")
    public ResponseEntity<Boolean> saveFavoriteJob(@RequestBody FavoritesJobsDto favoritesJobsDto) {
        // Extract details from DTO
        Long jobId = favoritesJobsDto.getJobId();
        Long companyId = favoritesJobsDto.getCompanyId();
        Long userId = favoritesJobsDto.getUserId();

        // Call the service to save favorite job
        favoritesJobsService.saveFavoritesJobs(jobId, companyId, userId);

        return ResponseEntity.ok(true);
    }

    // remove Favorite job REST API
    @DeleteMapping("/remove-favorite")    //localhost:8080/jobseeker/remove-favorite?jobId=8&companyId=2
    public ResponseEntity<String> removeFavoriteJob(@RequestParam Long jobId, @RequestParam Long companyId) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Find the user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Call the service to remove the favorite job
        favoritesJobsService.removeFavoriteJob(jobId, companyId, (long) user.getId());

        return ResponseEntity.ok("Job removed from favorites.");
    }

    // get favorite jobs REST API
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoritesJobsDto>> getFavoriteJobs() {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Find the user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Get the list of favorite jobs
        List<FavoritesJobsDto> favoriteJobs = favoritesJobsService.getFavoriteJobs((long) user.getId());

        return ResponseEntity.ok(favoriteJobs);
    }

    // check favorite job REST API
    @GetMapping("/is-favorite")  //localhost:8080/jobseeker/is-favorite?jobId=8&companyId=2
    public ResponseEntity<Boolean> isFavorite(@RequestParam Long jobId, @RequestParam Long companyId) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Find the user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Check if the job is a favorite
        Boolean isFavorite = favoritesJobsService.isFavorite(jobId, companyId, (long) user.getId());

        return ResponseEntity.ok(isFavorite);
    }


    // apply for job REST API
    @PostMapping("/apply")
    public ResponseEntity <ApplicantDto> saveApplication(@RequestBody ApplicantDto applicantDto){

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
