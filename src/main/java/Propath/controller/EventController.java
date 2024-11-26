package Propath.controller;

import Propath.dto.EventDto;
import Propath.dto.JobSeekerEventDto;
import Propath.service.JobSeekerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobseeker")
public class EventController {
    @Autowired
    private JobSeekerEventService jobSeekerEventService;
    @GetMapping("/getAllEvents")
    public List<JobSeekerEventDto> getAllEvents(){
        return jobSeekerEventService.getAllEvents();
    }

    @PostMapping("/registerEvent/{eventId}")
    public ResponseEntity<String> registerEvent(@PathVariable Long eventId){
        String output = jobSeekerEventService.registerEvent(eventId);
        return ResponseEntity.ok(output);
    }

    @GetMapping("getFullEventDetails")
    public List<EventDto> getFullEventDetails(){
        return jobSeekerEventService.getFullEventDetails();
    }

}
