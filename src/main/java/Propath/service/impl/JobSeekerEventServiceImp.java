package Propath.service.impl;

import Propath.dto.EventDto;
import Propath.dto.JobSeekerEventDto;
import Propath.mapper.EventMapper;
import Propath.mapper.JobSeekerEventMapper;
import Propath.model.*;
import Propath.repository.EventRepository;
import Propath.repository.JobSeekerEventRepository;
import Propath.repository.JobSeekerRepository;
import Propath.repository.UserRepository;
import Propath.service.EmailService;
import Propath.service.JobSeekerEventService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private EmailService emailService;




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
    public JobSeekerEventDto registerEvent(Long eventId, JobSeekerEventDto tokenDetails) {

        // Get the currently authenticated user
        String response = " ";
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


        LocalDateTime appliedDate = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String deadLineString = event.getCloseDate() + " " + event.getEndTime();
        LocalDateTime deadLine = LocalDateTime.parse(deadLineString,formatter);



        //Check whether user register before the deadlin
        if(appliedDate.isAfter(deadLine)){
            throw  new RuntimeException("Dedline passed");
        }
        //checks whether user registers before the event reach its max participants


        int applicantCount = event.getCurrentParticipants();

        if(applicantCount+1<=event.getMaxParticipant()){
            applicantCount++;
            event.setCurrentParticipants(applicantCount);

        }
        else{
            throw new RuntimeException("Maximum participant reached");
        }


        JobseekerEvent jobseekerEvent = new JobseekerEvent();

        jobseekerEvent.setEvent(event);
        jobseekerEvent.setJobSeeker(jobSeeker.get());
        jobseekerEvent.setAppliedDate(LocalDateTime.now());
        jobseekerEvent.setIsApplied(true);
        jobseekerEvent.setQrToken(tokenDetails.getQrToken());
        jobseekerEvent.setQrImg(tokenDetails.getQrImg());


        if(isAlreadyReg.isPresent()){
            jobseekerEvent.setRegistrationId(isAlreadyReg.get().getRegistrationId());
            jobseekerEvent.setQrToken(isAlreadyReg.get().getQrToken());
            jobseekerEvent.setQrImg(isAlreadyReg.get().getQrImg());

            if(isAlreadyReg.get().getIsApplied()){
                jobseekerEvent.setIsApplied(false);
                applicantCount = applicantCount-2;
                event.setCurrentParticipants(applicantCount);
                response = "Successfully leaved the event";
            }
            else{
                jobseekerEvent.setIsApplied(true);
                response = "Successfully joined the event";
            }
        }else{

            emailService.sendEventQR(event,tokenDetails);
            jobseekerEvent.setIsParticipate(false);
        }



        //JobseekerEvent jobseekerEvent = new JobseekerEvent(event,jobSeeker,LocalDateTime.now(),true);
        //eventRepository.save(event);
        eventRepository.save(event);
        jobSeekerEventRepository.save(jobseekerEvent);

        return JobSeekerEventMapper.maptoJobSeekerEventDto(jobseekerEvent);
    }

    @Override
    public List<EventDto> getFullEventDetails() {
        List<Event> eventDtos = eventRepository.findAll();
        return eventDtos.stream().map(EventMapper::maptoEventDto).collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        return EventMapper.maptoEventDto(event);
    }

    @Override
    public JobSeekerEventDto getJobSeekerEventById(long eventId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isEmpty()){
            throw new RuntimeException("user not found");
        }

        Optional<JobSeeker> jobSeeker = jobSeekerRepository.findByUser_Id(userOptional.get().getId());

        if(jobSeeker.isEmpty()){
            throw new RuntimeException("job seeker detail table, user not found");
        }


        //check user alredy registerd
        Optional<JobseekerEvent> isAlreadyReg = jobSeekerEventRepository.findByEvent_IdAndJobSeeker_Id(eventId,jobSeeker.get().getId());

        if(isAlreadyReg.isEmpty()){

            JobseekerEvent event = new JobseekerEvent(null,eventRepository.findById(eventId).orElse(null),null,null,false,null,null,null);
            return JobSeekerEventMapper.maptoJobSeekerEventDto(event);
        }else{

            //check boolean value
            JobseekerEvent event = isAlreadyReg.get();

            return JobSeekerEventMapper.maptoJobSeekerEventDto(event);

        }

    }

    @Override
    public JobSeekerEventDto getUserDetailsByTokenId(JobSeekerEventDto eventDto, Long eventId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isEmpty()){
            throw new RuntimeException("user not found");
        }

        Optional<JobseekerEvent> regDetais = jobSeekerEventRepository.findByQrTokenAndEvent_IdAndIsAppliedTrue(eventDto.getQrToken(),eventId);

        if(regDetais.isEmpty()){
            return null;
        }else{

            JobseekerEvent userDetails = regDetais.get();


            return JobSeekerEventMapper.maptoJobSeekerEventDto(userDetails);


        }

    }


    @Override
    public void UpdateParticipantStatus(JobSeekerEventDto eventDto){

        eventDto.setIsParticipate(true);
        jobSeekerEventRepository.save(JobSeekerEventMapper.maptoJobSeekerEvent(eventDto));
    }

    @Override
    public List<JobSeekerEventDto>  getEventBySeekerId(Long id) {
        List<JobseekerEvent> jobseekerEvents = jobSeekerEventRepository.findByJobSeekerUser_Id(id);
        return jobseekerEvents.stream().filter((event)->event.getIsApplied()).map((event)->{
            return JobSeekerEventMapper.maptoJobSeekerEventDto(event);
            
            }).toList();
    }


}
