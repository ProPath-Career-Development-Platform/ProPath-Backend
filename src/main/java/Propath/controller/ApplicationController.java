package Propath.controller;

import Propath.dto.ApplicantDto;
import Propath.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class ApplicationController {


    @Autowired
    private ApplicantService applicantService;

    @GetMapping("myjobs/{jobId}/applications")
    public ResponseEntity<List<ApplicantDto>> getApplicants(@PathVariable("jobId") Long jobId){
        List<ApplicantDto> applicants = applicantService.getApplicants(jobId);
        return new ResponseEntity<>(applicants, HttpStatus.OK);
    }

    @PostMapping("/applicant/selected")
    public ResponseEntity<List<ApplicantDto>> getApplicantDetails(@RequestBody List<Integer> ids) {
        List<ApplicantDto> applicants = applicantService.getApplicantsByUserIds(ids);
        return ResponseEntity.ok(applicants);
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
}
