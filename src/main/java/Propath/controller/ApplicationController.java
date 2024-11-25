package Propath.controller;

import Propath.dto.ApplicantDto;
import Propath.dto.ApplicantRequestDto;
import Propath.model.JobSeeker;
import Propath.repository.JobSeekerRepository;
import Propath.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class ApplicationController {


    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @GetMapping("myjobs/{jobId}/applications")
    public ResponseEntity<List<Map<String,Object>>> getApplicants(@PathVariable("jobId") Long jobId){
        List<ApplicantDto> applicants = applicantService.getApplicants(jobId);


        List<Map<String, Object>> appliedUsers = new ArrayList<>();

        for( ApplicantDto applicant : applicants){
            Map<String, Object> application = new HashMap<>();

            Optional<JobSeeker> seekerPersonalInfo = jobSeekerRepository.findByUser_Id(applicant.getUser().getId());

            application.put("id",applicant.getId());
            application.put("name",applicant.getUser().getName());
            application.put("atsScore",applicant.getAtsScore());
            application.put("appliedDate",applicant.getAppliedDate());
            application.put("status",applicant.getStatus());
            application.put("response",applicant.getResponse());
            application.put("email",applicant.getEmail());
            application.put("seekerId",applicant.getUser().getId());
            application.put("jobId",applicant.getJob().getId());
            application.put("cv", applicant.getCv());
            application.put("exp","senior"); // hardcode , need to create in jobseeker table

            if(seekerPersonalInfo.isEmpty()){
                application.put("proUrl", null);
            }else{
                application.put("proUrl", seekerPersonalInfo.get().getProfilePicture());
            }


            appliedUsers.add(application);
        }

        return new ResponseEntity<>(appliedUsers,HttpStatus.OK);


    }

    @PostMapping("/applicant/selected")
    public ResponseEntity<List<Map<String,Object>>> getApplicantDetails(@RequestBody ApplicantRequestDto request ) {

     //   List<ApplicantDto> applicants = applicantService.getApplicantsByUserIds(ids,jobId);
        List<ApplicantDto> applicants = applicantService.getApplicantsByUserIds(request.getIds(), request.getJobId());


        List<Map<String, Object>> appliedUsers = new ArrayList<>();

        for( ApplicantDto applicant : applicants){
            Map<String, Object> application = new HashMap<>();

            Optional<JobSeeker> seekerPersonalInfo = jobSeekerRepository.findByUser_Id(applicant.getUser().getId());

            application.put("id",applicant.getId());
            application.put("name",applicant.getUser().getName());
            application.put("atsScore",applicant.getAtsScore());
           // application.put("appliedDate",applicant.getAppliedDate());
            application.put("status",applicant.getStatus());
           // application.put("response",applicant.getResponse());
            application.put("email",applicant.getEmail());
            application.put("seekerId",applicant.getUser().getId());
            application.put("jobId",applicant.getJob().getId());
          //  application.put("cv", applicant.getCv());
          //  application.put("exp","senior"); // hardcode , need to create in jobseeker table

            if(seekerPersonalInfo.isEmpty()){
                application.put("proUrl", null);
            }else{
                application.put("proUrl", seekerPersonalInfo.get().getProfilePicture());
            }


            appliedUsers.add(application);
        }

        return new ResponseEntity<>(appliedUsers,HttpStatus.OK);

    }

    @PostMapping("/applicant")
    public ResponseEntity <ApplicantDto> saveApplication(@RequestBody ApplicantDto applicantDto){

        ApplicantDto savedAppliation = applicantService.saveApplication(applicantDto);

        return new ResponseEntity<>(savedAppliation,HttpStatus.OK);
    }

    @PutMapping("/applicant/updateStatus/{seekerId}/{jobId}")
    public ResponseEntity<String> UpdateStatus (@PathVariable Integer seekerId , @PathVariable Long jobId){

        Boolean isUpdated = applicantService.updateStatusToPending(seekerId,jobId);

        if (isUpdated) {
            return ResponseEntity.ok("Status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update status");
        }

    }

    @PutMapping("applicant/updateStatusPreSelected/{jobId}")
    public ResponseEntity<String> UpdateStatusPreSelected(@PathVariable Long jobId ,@RequestBody List<Integer> ids){

        Boolean UpdatePreSelected = applicantService.updateStatusToPreSelected(ids,jobId);

        if(UpdatePreSelected){
            return ResponseEntity.ok("Status Update Successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update status");
        }
    }

    @PutMapping("applicant/updateStatusSelected/{jobId}")
    public ResponseEntity<String> updateStatusSelected(@PathVariable Long jobId ,@RequestBody List<Integer> ids){

        Boolean UpdatePreSelected = applicantService.updateStatusToSelected(ids,jobId);

        if(UpdatePreSelected){
            return ResponseEntity.ok("Status Update Successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update status");
        }
    }
}
