package Propath.controller;

import Propath.dto.*;
import Propath.model.AuthenticationResponse;
import Propath.model.JobseekerEvent;
import Propath.model.SubscriptionPlan;
import Propath.model.User;
import Propath.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class JobProviderController {
    private static final Logger logger = LoggerFactory.getLogger(JobProviderController.class);

    @Autowired
    private JobProviderService jobProviderService;

    @Autowired
    private EventService eventService;

    @Autowired
    private JobService jobService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private JobSeekerEventService jobSeekerEventService;




    @PostMapping
    public ResponseEntity<JobProviderDto> registerJobProvider(@RequestBody JobProviderDto jobProviderDto) {
        JobProviderDto savedJobProvider = jobProviderService.saveJobProvider(jobProviderDto);
        return new ResponseEntity<>(savedJobProvider, HttpStatus.CREATED);
    }

    @GetMapping("/event")
    public ResponseEntity <List<EventDto>> getAllEvent(){

        
        List<EventDto> events = eventService.getAllEvent();
        // Transform the fetched EventDto objects to set user details to an empty string
        List<EventDto> transformedEvents = events.stream()
                .map(event -> {
                    event.setUser(null); // Assuming there's a setUser method to set the user details
                    return event;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(transformedEvents, HttpStatus.OK);

    }

    @PostMapping("/event")
    public ResponseEntity<EventDto> crateAnEvent (@RequestBody EventDto eventDto){
        EventDto savedEvent = eventService.saveEvent(eventDto);
        return new ResponseEntity<>(savedEvent,HttpStatus.CREATED);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") Long id){
        EventDto requestEvent = eventService.getEventById(id);

        requestEvent.setUser(null);

        return new ResponseEntity<>(requestEvent,HttpStatus.OK);

    }

    @PutMapping("/event/{id}")
    public ResponseEntity<EventDto> updateEvent (@PathVariable("id") Long id , @RequestBody EventDto updateEventDto){
        EventDto updatedEventDto = eventService.updateEvent(id,updateEventDto);
        return new ResponseEntity<>(updatedEventDto, HttpStatus.OK);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> removeEvent(@PathVariable("id") Long id){

        eventService.removeEvent(id);

        return new ResponseEntity<>("Event deleted successfully" + id , HttpStatus.OK);
    }

    //Jobs

    @GetMapping("/job")
    public ResponseEntity<List<JobDto>> getAllJobs(){

        List<JobDto> jobs = jobService.getJobs();


        List<JobDto> transformedJobs = jobs.stream()
                .map(event -> {
                    event.setUser(null); // Assuming there's a setUser method to set the user details
                    return event;
                })
                .sorted(Comparator.comparing(JobDto::getId))
                .collect(Collectors.toList());

        return new ResponseEntity<>(transformedJobs,HttpStatus.OK);

    }

    @PostMapping("/job")
    public ResponseEntity<JobDto> createJob (@RequestBody JobDto jobDto){

        JobDto saveJob = jobService.saveJob(jobDto);

        return new ResponseEntity<>(saveJob,HttpStatus.CREATED);


    }

    @GetMapping("/job/{id}")
    public ResponseEntity<JobDto> getJobByID(@PathVariable("id") Long id){

        JobDto job = jobService.getJobById(id);

        job.setUser(null);

        return new ResponseEntity<>(job,HttpStatus.OK);
    }

    @GetMapping("/job/{jobId}/applicant/{userId}")
    public ResponseEntity<Map<String,Object>> getResponse(@PathVariable("jobId") Long jobId , @PathVariable("userId") Integer userId){

        ApplicantDto applicant = applicantService.getFormResponse(jobId,userId);
        Map<String, Object> application = new HashMap<>();

        if(applicant.getJob().getCustomizedForm() == null){
            application.put("response", "form-not-found");
            return new ResponseEntity<>(application, HttpStatus.OK);
        }else {

            if(applicant.getResponse() != null) {
                application.put("response", applicant.getResponse());
                return new ResponseEntity<>(application, HttpStatus.OK);
            }else{
                application.put("response", "no-response");
                return new ResponseEntity<>(application, HttpStatus.OK);
            }
        }

    }

    @PutMapping("/job/status/expire/{id}")
    public ResponseEntity<String> updateJobStautsToExpire(@PathVariable("id") Long id){

        Boolean result = jobService.setExpireJob(id);

        if (result) {
            return ResponseEntity.ok("Status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update status");
        }


    }

    @PutMapping("/job/update/{id}")
    public ResponseEntity<JobDto> updateJob(@PathVariable("id") Long id, @RequestBody JobDto jobDto){

       JobDto updateJob = jobService.updateJob(id,jobDto);

       return new ResponseEntity<>(updateJob,HttpStatus.OK);


        
    }

    @PostMapping("/send/v-email")
    public ResponseEntity<Boolean> sendVerification(@RequestBody VerficationTokenDto verficationTokenDto){

        Boolean result = emailService.settingsEmailVerification(verficationTokenDto);

        if (result) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

    }

    @PostMapping("/verify-email/{token}")
    public ResponseEntity<?> checkVerification(@PathVariable("token") String token) {

        try {
            AuthenticationResponse result = emailService.checkVerification(token);
            return ResponseEntity.ok(result); // Return the AuthenticationResponse with the new JWT
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Verification failed: " + e.getMessage());
        }
    }

    @PostMapping("/update/personal/name")
    public ResponseEntity<Boolean> updatePersonalName(@RequestBody User user) {

        Boolean result = jobProviderService.updatePersonalName(user);

        if (result) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

    }

    @GetMapping("/event/register/{id}")
    public ResponseEntity<List<Map<String,Object>>> getRegisterdUsersByEventId(@PathVariable("id") Long id){

        List<JobSeekerEventDto> jobSeekerEventDtos = eventService.getRegisteredusers(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a");
        List<Map<String, Object>> registerUsers = new ArrayList<>();

        for(JobSeekerEventDto jobSeekerEventDto : jobSeekerEventDtos){
            Map<String, Object> registerUser = new HashMap<>();

            registerUser.put("userId", jobSeekerEventDto.getJobSeeker().getUser().getId());
            registerUser.put("userName", jobSeekerEventDto.getJobSeeker().getUser().getUsername());
            registerUser.put("userEmail", jobSeekerEventDto.getJobSeeker().getUser().getEmail());
            registerUser.put("profilePicture", jobSeekerEventDto.getJobSeeker().getProfilePicture());
            registerUser.put("appliedDate", jobSeekerEventDto.getAppliedDate().format(formatter));
            registerUser.put("IsApplied", jobSeekerEventDto.getIsApplied());
            registerUser.put("IsParticipate", jobSeekerEventDto.getIsParticipate());
            registerUser.put("qrImg", jobSeekerEventDto.getQrImg());
            registerUser.put("regID", jobSeekerEventDto.getId());

            registerUsers.add(registerUser);

        }

        registerUsers.sort(Comparator.comparing(user -> (Long) user.get("regID")));

        return new ResponseEntity<>(registerUsers,HttpStatus.OK);

    }

    @GetMapping("/subscription")
    public ResponseEntity<Map<String,Object>> getUserSubcriptionDetails(){

        UserSubscriptionDto userSubscriptionDto = userSubscriptionService.getSubscription();

        Map<String, Object> subDes = new HashMap<>();

        subDes.put("userId", userSubscriptionDto.getUser().getId());
        subDes.put("planName", userSubscriptionDto.getSubscriptionPlan().getPlanName());
        subDes.put("planPrice", userSubscriptionDto.getSubscriptionPlan().getPrice());
        subDes.put("planStartDate", userSubscriptionDto.getStartDate());
        subDes.put("planEndDate", userSubscriptionDto.getEndDate());
        subDes.put("planCreatedAt", userSubscriptionDto.getCreatedAt());
        subDes.put("paidStatus", userSubscriptionDto.getPaidStatus());
        subDes.put("planId", userSubscriptionDto.getSubscriptionPlan().getId());

        return new ResponseEntity<>(subDes, HttpStatus.OK);


    }

    @GetMapping("/subscription/plan")
    public ResponseEntity<List<SubscriptionPlanDto>> getPlanDetails(){

        List<SubscriptionPlanDto> plans = userSubscriptionService.getSubscriptionPlans();

        return new ResponseEntity<>(plans, HttpStatus.OK);

    }

    @GetMapping("/check-subscripton")
    public ResponseEntity<Boolean> checkSubscription(){

        Boolean result = userSubscriptionService.isPlanExpired();

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/analysis/job/barchart")
    public ResponseEntity<List<Map<String,Object>>> GetActiveJobs(){

        List<JobDto> jobs = jobService.getActiveJobs();

        List<Map<String, Object>> b_jobs = new ArrayList<>();

        for(JobDto jobDto : jobs){

            Map<String, Object> job = new HashMap<>();

            job.put("jobId", jobDto.getId());
            job.put("jobTitle", jobDto.getJobTitle());
            job.put("jobApplicantCount", jobDto.getApplicantCount());

            b_jobs.add(job);
        }

        return new ResponseEntity<>(b_jobs,HttpStatus.OK);



    }

    @GetMapping("/analysis/event/barchart")
    public ResponseEntity<List<Map<String,Object>>> GetActiveEvents(){

        List<EventDto> eventDtos = eventService.getActveEventsWithUserId();

        List<Map<String, Object>> b_events = new ArrayList<>();

        for(EventDto eventDto : eventDtos){

            Map<String, Object> event = new HashMap<>();

            List<JobSeekerEventDto> count  = eventService.getRegisteredusers(eventDto.getId());

            event.put("eventId", eventDto.getId());
            event.put("eventTitle", eventDto.getTitle());
            event.put("eventParticipants", count.size());

            b_events.add(event);
        }

        return new ResponseEntity<>(b_events,HttpStatus.OK);



    }

    @PostMapping("/participant/verify/{eventId}")
    public ResponseEntity<Map<String,Object>> verifyParticipant(@RequestBody JobSeekerEventDto tokenDetails,@PathVariable("eventId") Long eventId){

        JobSeekerEventDto event = jobSeekerEventService.getUserDetailsByTokenId(tokenDetails,eventId);
//2024-12-01 19:58:58.729994
        Map<String, Object> userDetails = new HashMap<>();

        if(event == null){
            userDetails.put("verified", false);

        }else{

            if(event.getIsParticipate()){

                userDetails.put("participate", true);
                userDetails.put("verified", false);
            }else{
                userDetails.put("userId", event.getJobSeeker().getUser().getId());
                userDetails.put("userName", event.getJobSeeker().getUser().getUsername());
                userDetails.put("userProPic", event.getJobSeeker().getProfilePicture());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy, hh:mm a");
                String formattedDate = event.getAppliedDate().format(formatter);
                userDetails.put("enrollDate", formattedDate);



                userDetails.put("userEmail", event.getJobSeeker().getUser().getEmail());
                userDetails.put("verified", true);
                userDetails.put("participate", false);

                jobSeekerEventService.UpdateParticipantStatus(event);

            }

        }
        return new ResponseEntity<>(userDetails, HttpStatus.OK);


    }











}