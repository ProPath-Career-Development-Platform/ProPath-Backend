package Propath.controller;

import Propath.dto.EventDto;
import Propath.dto.JobProviderDto;
import Propath.model.Event;
import Propath.service.EventService;
import Propath.service.JobProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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





}