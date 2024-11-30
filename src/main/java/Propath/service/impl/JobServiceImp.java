package Propath.service.impl;

import Propath.exception.ResourceNotFoundException;
import Propath.dto.CompanyDto;
import Propath.dto.JobDto;

import Propath.mapper.JobMapper;

import Propath.model.Applicant;
import Propath.model.Company;
import Propath.model.Job;
import Propath.model.User;

import Propath.repository.ApplicantRepository;
import Propath.repository.CompanyRepository;
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

    private JobMapper JobMapper;

    private CompanyRepository companyRepository;

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

    @Override
    public JobDto getJobByIdJs(Long id) {

        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job Not Found"));
        JobDto jobDto = JobMapper.maptoJobDto(job);
        Company company = companyRepository.findByUser(job.getUser()).orElseThrow(()-> new RuntimeException("No Company Found"));
        jobDto.setCompany(company);
        return jobDto;
    }
    @Override
    public List<JobDto> getAllJobs(List<String> filter, String jobType) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
         List<Job> jobs;
        if(filter.isEmpty()){
             jobs =  jobRepository.findAll();

        }
        else{
            if(jobType.equals("Job Type")) {
                jobs = jobRepository.findByJobTypeIn(filter);
            }
            else if(jobType.equals("Experience")){
                jobs = jobRepository.findByExperienceIn(filter);

            }
            else if(jobType.equals("Job Role")){
                jobs = jobRepository.findByJobRoleIn(filter);

            }
            else{
                jobs =  jobRepository.findAll();

            }

        }

        return jobs.stream().map((job)-> {


            Company company = companyRepository.findByUser(job.getUser()).orElseThrow(()->new RuntimeException("Company Id not found"));
            JobDto jobDto = JobMapper.maptoJobDto(job);

            jobDto.setCompany(company);
            return jobDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<JobDto> getAllPostedJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map((job) -> JobMapper.maptoJobDto(job)).collect(Collectors.toList());
    }

    @Override
    public JobDto getPostJobById(Long postId) {
        Job job = jobRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
       return JobMapper.maptoJobDto(job);
    }

    @Override
    public List<JobDto> getAllPostJobs() {
        List<Job> postedJobs = jobRepository.findAll();
        return postedJobs.stream()
                .map(job -> {
                    JobDto dto = JobMapper.maptoJobDto(job);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyInfoByJobId(Long jobId) {
        // Step 1: Retrieve the job by jobId
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + jobId));

        // Step 2: Retrieve the associated user (job provider)
        User user = job.getUser();

        // Step 3: Retrieve the company associated with the user
        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user with id " + user.getId()));

        // Step 4: Map Company to CompanyDto and return
        return new CompanyDto(
                company.getId(),
                company.getCompanyName(),
                company.getAboutUs(),
                company.getLogoImg(),
                company.getBannerImg(),
                company.getOrganizationType(),
                company.getIndustryType(),
                company.getEstablishedDate() != null ? company.getEstablishedDate().toString() : null,
                company.getCompanyWebsite(),
                company.getCompanyVision(),
                company.getLocation(),
                company.getContactNumber(),
                company.getEmail(),
                company.getXUrl(),
                company.getFbUrl(),
                company.getLinkedinUrl(),
                company.getYoutubeUrl(),
                null,        // pwd, set to null for security
                null,        // newPwd, set to null for security
                company.getIsNew(),
                company.getStatus(),
                company.getUser()
        );
    }
    @Override
    public List<JobDto> getRelatedJobsByTags(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        List<String> tags = job.getTags();
        if (tags == null || tags.isEmpty()) {
            throw new RuntimeException("No tags found for Job ID: " + jobId);
        }
        List<Job> relatedJobs = jobRepository.findJobsByMatchingTags(tags.toArray(new String[0]), jobId);
        return relatedJobs.stream().map(relatedJob -> {
            JobDto jobDto = JobMapper.maptoJobDto(relatedJob);
            Company company = companyRepository.findByUser(relatedJob.getUser()).orElseThrow(() -> new RuntimeException("Company not found for user with id " + relatedJob.getUser().getId()));
            jobDto.setCompany(company);
            return jobDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<JobDto> getActiveJobs(){

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));



        List<Job> jobs = jobRepository.findByUserAndStatus(user,"active");

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





}
