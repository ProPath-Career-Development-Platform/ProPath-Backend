package Propath.controller;

import Propath.dto.EventDto;
import Propath.dto.JobSeekerEventDto;
import Propath.service.JobSeekerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String,Object>> registerEvent(@PathVariable Long eventId,@RequestBody JobSeekerEventDto tokenDetais) {



        JobSeekerEventDto jobSeekerEventDto = jobSeekerEventService.registerEvent(eventId,tokenDetais);

        Map<String, Object> event = new HashMap<>();

        event.put("jobSeekerId", jobSeekerEventDto.getJobSeeker().getUser().getId());
        event.put("eventId", jobSeekerEventDto.getEvent().getId());
        event.put("isApplied", jobSeekerEventDto.getIsApplied());
        event.put("appliedDate", jobSeekerEventDto.getAppliedDate());
        event.put("currentParticipants", jobSeekerEventDto.getEvent().getCurrentParticipants());
        event.put("maxParticipant", jobSeekerEventDto.getEvent().getMaxParticipant());

        return new ResponseEntity<>(event,HttpStatus.OK);


    }

    @GetMapping("getFullEventDetails")
    public List<EventDto> getFullEventDetails(){
        return jobSeekerEventService.getFullEventDetails();
    }

}
