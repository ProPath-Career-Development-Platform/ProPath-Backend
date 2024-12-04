package Propath.service.impl;

import Propath.dto.CompanyDto;
import Propath.dto.JobSeekerDto;
import Propath.mapper.CompanyMapper;
import Propath.mapper.JobSeekerMapper;
import Propath.model.*;
import Propath.repository.*;
import Propath.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobSeekerImp implements JobSeekerService {
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobSeekerEventRepository jobSeekerEventRepository;
    @Override
    public JobSeekerDto getJobSeekerDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new RuntimeException("Email Not found"));

        JobSeeker job = new JobSeeker();
        job.setUser(user);
        return JobSeekerMapper.mapToJobSeekerDto(job);
    }

    @Override
    public List<JobSeekerDto> getJobSeekers() {
        List<JobSeeker> jobSeekers = jobSeekerRepository.findAll();
        return jobSeekers.stream().map((jobSeeker) -> JobSeekerMapper.mapToJobSeekerDto(jobSeeker)).collect(Collectors.toList());
    }

    @Override
    public List<List<String>> getNotifications() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        int id = userOptional.get().getId();



        List<Job> jobList = jobRepository.findTop3ByOrderByPostedIn();
        List<List<String>> output = new ArrayList<>();
        jobList.stream()
                .forEach(job -> {
                   String description =  job.getJobTitle() + " " ;
                   String type = "newJob";
                   String date = job.getPostedIn();
                   List<String> outpu1 = List.of(description,type,date);
                   output.add(outpu1);
                });

        List<Event> eventList = eventRepository.findTop3ByOrderByDate();
        eventList.stream()
                .forEach(event->{
                    String title = event.getTitle() + " at " + event.getLocation();
                    String type = "newEvent";
                    String date = event.getDate();
                    List<String> output1 = List.of(title,type,date);
                    output.add(output1);
                });
        List<JobseekerEvent> jobseekerEvents = jobSeekerEventRepository.findByJobSeekerUser_Id(id);
        jobseekerEvents.forEach(jobseekerEvent->{
            Event event = jobseekerEvent.getEvent();
            String title = event.getTitle() + " " + event.getLocation();
            String type = "upcoming Events";
            String date = event.getDate();
            List<String> output1 = List.of(title,type,date);
            output.add(output1);
        });
        return output;
    }
}
