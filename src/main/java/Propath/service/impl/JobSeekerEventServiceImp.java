package Propath.service.impl;

import Propath.dto.JobSeekerEventDto;
import Propath.mapper.JobSeekerEventMapper;
import Propath.mapper.JobSeekerMapper;
import Propath.model.*;
import Propath.repository.EventRepository;
import Propath.repository.JobSeekerEventRepository;
import Propath.repository.JobSeekerRepository;
import Propath.repository.UserRepository;
import Propath.service.JobSeekerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class JobSeekerEventServiceImp implements JobSeekerEventService {

    @Autowired
    private JobSeekerEventRepository jobSeekerEventRepository;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;


    @Autowired
    private EventRepository eventRepository;


    @Override
    public List <JobSeekerEventDto> getAllEvents() {
        List<JobseekerEvent> jobSeekerEvents = jobSeekerEventRepository.findAll();
        return jobSeekerEvents.stream().map(
               jobseekerEvent -> {
                   JobSeekerEventDto jobSeekerEventDto = JobSeekerEventMapper.maptoJobSeekerEventDto(jobseekerEvent);
                   return jobSeekerEventDto;
               }
        ).collect(Collectors.toList());

    }

    @Override
    public String registerEvent(int seekerId, Long eventId) {

        JobSeeker jobSeeker = jobSeekerRepository.findById(seekerId).orElseThrow(()-> new RuntimeException("jobSeekerNotFound"));

        Event event = eventRepository.findById(eventId).orElseThrow(()-> new RuntimeException("eventNotFound"));

        LocalDateTime appliedDate = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String deadLineString = event.getCloseDate() + " " + event.getEndTime();
        LocalDateTime deadLine = LocalDateTime.parse(deadLineString,formatter);



        //Check whether user register before the deadlin
        if(appliedDate.isAfter(deadLine)){
            return "Event is already over !!!";
        }
        //checks whether user registers before the event reach its max participants


        int applicantCount = event.getCurrentParticipants();

        if(applicantCount+1<=event.getMaxParticipant()){
            applicantCount++;
            event.setCurrentParticipants(applicantCount);
        }
        else{
            return "Enrollment is closed as the maximum number of participants has been reached.";
        }






        JobseekerEvent jobseekerEvent = new JobseekerEvent(event,jobSeeker,LocalDateTime.now(),true);
        eventRepository.save(event);
        jobSeekerEventRepository.save(jobseekerEvent);
        return "Event Registration is successful";
    }


}
