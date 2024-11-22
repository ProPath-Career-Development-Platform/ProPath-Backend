package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.dto.EventDto;
import Propath.dto.JobDto;
import Propath.dto.JobSeekerDto;
import Propath.service.CompanyService;
import Propath.service.EventService;
import Propath.service.JobSeekerService;
import Propath.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private JobService jobService;

    @Autowired
    private EventService eventService;

    @Autowired   // This annotation is optional for constructor-based injection
    public AdminController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/RegisterdCompanies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
       List<CompanyDto> companies = companyService.getALLCompanies();
       return ResponseEntity.ok(companies);
    }

    @PutMapping("/RegisterdCompanies/verify/{id}")
    public ResponseEntity<CompanyDto> updateCompanyStatus(@PathVariable("id") Long id,
                                                          @RequestBody CompanyDto updatedCompany) {
       CompanyDto companyDto = companyService.updateCompanyStatus(id, updatedCompany);
       return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/companyRequests")
    public ResponseEntity<List<CompanyDto>> getAllRequests() {
        List<CompanyDto> requests = companyService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/companyRequests/{id}/approve")
    public ResponseEntity<?> approveCompany(@PathVariable int id) {
        CompanyDto updatedCompany = companyService.approveCompany(id);
        return ResponseEntity.ok(updatedCompany);
    }

    @GetMapping("/numberofPendingReq")
    public ResponseEntity<Integer> getPendingRequestsCount() {
        int count = companyService.getPendingRequestsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/RegisterdUsers")
    public ResponseEntity<List<JobSeekerDto>> getAllJobSeekers() {
        List<JobSeekerDto> jobseekers = jobSeekerService.getJobSeekers();
        return ResponseEntity.ok(jobseekers);
    }

    @GetMapping("/PostedJobs")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        List<JobDto> jobs = jobService.getAllPostedJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/Events")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.getAllPostedEvent();
        return ResponseEntity.ok(events);
    }

}
