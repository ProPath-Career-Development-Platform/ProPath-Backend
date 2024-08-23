package Propath.service.impl;

import Propath.dto.ApplicantDto;
import Propath.dto.JobDto;
import Propath.mapper.ApplicantMapper;
import Propath.mapper.JobMapper;
import Propath.model.Applicant;
import Propath.model.Job;
import Propath.model.User;
import Propath.repository.ApplicantRepository;
import Propath.repository.JobRepository;
import Propath.repository.UserRepository;
import Propath.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JobServiceImp implements JobService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private ApplicantRepository applicantRepository;

    @Override
    public JobDto saveJob(JobDto jobDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();  // Get the username of the logged-in user

        // Assuming you have a method to find the user by username
        Optional<User> userOptional = userRepository.findByEmail(userEmail); // Implement this method in your UserRepository

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found"); // Handle the case when the user is not found
        }

        User user = userOptional.get();

        Job job = JobMapper.maptoJob(jobDto);

        job.setUser(user);
        System.out.println("Event Details: " + job);

        Job savedJob = jobRepository.save(job);

        return JobMapper.maptoJobDto(savedJob);


    }

    @Override
    public List<JobDto> getJobs(){

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        List<Job> jobs = jobRepository.findByUserIdAndDeleteFalse(user.getId());

        return jobs.stream()
                .map(JobMapper::maptoJobDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobDto getJobById(Long id){

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findByIdAndDeleteFalse(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check if the event belongs to the currently authenticated user
        if (job.getUser().getId() != (user.getId())) {
            throw new RuntimeException("Unauthorized request: user does not own this job.");
        }

        return JobMapper.maptoJobDto(job);
    }


}
