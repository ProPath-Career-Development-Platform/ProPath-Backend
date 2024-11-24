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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class JobSeekerEventServiceImp implements JobSeekerEventService {



    private  UserRepository userRepository;
    private  JobSeekerRepository jobSeekerRepository;
    private  EventRepository eventRepository;
    private  JobSeekerEventRepository jobSeekerEventRepository;




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
    public String registerEvent(Long eventId) {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isEmpty()){
            throw new RuntimeException("user not found");
        }

        Optional<JobSeeker> jobSeeker = jobSeekerRepository.findByUser_Id(userOptional.get().getId());

        if(jobSeeker.isEmpty()){
            throw new RuntimeException("Event not found");
        }



        Event event = eventRepository.findById(eventId).orElseThrow(()-> new RuntimeException("eventNotFound"));


        //check user alredy registerd
        Optional<JobseekerEvent> isAlreadyReg = jobSeekerEventRepository.findByEvent_IdAndJobSeeker_Id(eventId,jobSeeker.get().getId());

        if(isAlreadyReg.isPresent()){
            throw new RuntimeException("User Alredy registerd");
        }

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


        JobseekerEvent jobseekerEvent = new JobseekerEvent();

        jobseekerEvent.setEvent(event);
        jobseekerEvent.setJobSeeker(jobSeeker.get());
        jobseekerEvent.setAppliedDate(LocalDateTime.now());
        jobseekerEvent.setIsApplied(true);




        //JobseekerEvent jobseekerEvent = new JobseekerEvent(event,jobSeeker,LocalDateTime.now(),true);
        //eventRepository.save(event);
        jobSeekerEventRepository.save(jobseekerEvent);
        return "Event Registration is successful";
    }


}
