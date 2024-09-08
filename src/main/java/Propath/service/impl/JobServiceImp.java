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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                .map(job-> {

                    JobDto dto = JobMapper.maptoJobDto(job);

                    //getApplicant count
                    List<String> statuses = Arrays.asList("pending", "preSelected","selected");
                    List<Applicant> applicant = applicantRepository.findByJobIdAndStatusIn(dto.getId(),statuses);

                    dto.setApplicantCount((Integer) applicant.size());
                    return dto;




                })
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

    @Override
    public Boolean setExpireJob(Long id){

       try { // Get the currently authenticated user
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

           job.setStatus("expire");

           LocalDate today = LocalDate.now();
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
           String formattedDate = today.format(formatter);


           job.setExpiryDate(formattedDate);

           jobRepository.save(job);

           return true;
       } catch (RuntimeException e){
           return false;
       }




    }

    @Override

    public JobDto updateJob(Long jobId, JobDto jobDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findByIdAndDeleteFalse(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));


        // Check if the job belongs to the currently authenticated user
        if (job.getUser().getId() != (user.getId())) {
            throw new RuntimeException("Unauthorized request: user does not own this job.");
        }

        // Update the existing job with new values from jobDto
        job.setJobTitle(jobDto.getJobTitle());
        job.setTags(jobDto.getTags());
        job.setJobRole(jobDto.getJobRole());
        job.setMinSalary(jobDto.getMinSalary());
        job.setMaxSalary(jobDto.getMaxSalary());
        job.setSalaryType(jobDto.getSalaryType());
        job.setEducation(jobDto.getEducation());
        job.setExperience(jobDto.getExperience());
        job.setJobType(jobDto.getJobType());
        job.setVacancies(jobDto.getVacancies());
        job.setExpiryDate(jobDto.getExpiryDate()); // Fixed typo
        job.setJobLevel(jobDto.getJobLevel());
        job.setJobDescription(jobDto.getJobDescription());
        job.setCustomizedForm(jobDto.getCustomizedForm()); // Fixed typo

        // Save the updated job
        Job updatedJob = jobRepository.save(job);

        // Optionally, convert updatedJob to JobDto and return it
        return JobMapper.maptoJobDto(updatedJob);

    }

    public List<Integer> findJobIdsByProviderId(int providerId){

        User user = userRepository.findById(providerId)
                .orElseThrow(()->new RuntimeException("User not Found"));

        return jobRepository.findJobIdsByProviderId(providerId);


    }


}
