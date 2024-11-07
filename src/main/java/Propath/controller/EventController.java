package Propath.controller;

import Propath.dto.JobSeekerEventDto;
import Propath.service.JobSeekerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private JobSeekerEventService jobSeekerEventService;
    @GetMapping("/getAllEvents")
    public List<JobSeekerEventDto> getAllEvents(){
        return jobSeekerEventService.getAllEvents();
    }

    @PostMapping("/registerEvent")
    public ResponseEntity<String> registerEvent(@RequestParam int seekerId , @RequestParam Long eventId){
        String output = jobSeekerEventService.registerEvent(seekerId,eventId);
        return ResponseEntity.ok(output);
    }


}
