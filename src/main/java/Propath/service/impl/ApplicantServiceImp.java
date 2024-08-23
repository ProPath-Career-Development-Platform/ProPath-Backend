package Propath.service.impl;

import Propath.dto.ApplicantDto;
import Propath.mapper.ApplicantMapper;
import Propath.model.Applicant;
import Propath.model.Job;
import Propath.model.User;
import Propath.repository.ApplicantRepository;
import Propath.repository.JobRepository;
import Propath.repository.UserRepository;
import Propath.service.ApplicantService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplicantServiceImp implements ApplicantService {

    private ApplicantRepository applicantRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;

    @Override
    public List<ApplicantDto> getApplicants(Long jobId) {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job id not found"));

        if(job.getUser().getId() != (user.getId())){
            throw new RuntimeException("Unauthorized request: user does not own this job.");
        }

        List<String> statuses = Arrays.asList("pending", "preSelected");
        List<Applicant> applicants = applicantRepository.findByJobIdAndStatusIn(jobId,statuses);

        // Map to ApplicantDto and set email
        return applicants.stream()
                .map(applicant -> {
                    ApplicantDto dto = ApplicantMapper.mapToApplicantDto(applicant);
                   dto.setEmail(applicant.getUser().getEmail());
                   dto.setExp("Senior");
                   dto.setName(applicant.getUser().getName());
                   dto.setSeekerId(applicant.getUser().getId());
                   dto.setJob(null);
                   dto.setUser(null);
                   // Set the email from the User
                    return dto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<ApplicantDto> getApplicantsByUserIds(List<Integer> userIds) {
        List<Applicant> applicants = applicantRepository.findAllByUserIdIn(userIds);
        return applicants.stream()
                .map(applicant -> {
                    ApplicantDto dto = ApplicantMapper.mapToApplicantDto(applicant);

                    // Null checks for the User object
                    if (applicant.getUser() != null) {
                        dto.setEmail(applicant.getUser().getEmail());
                        dto.setName(applicant.getUser().getName());
                        dto.setSeekerId(applicant.getUser().getId());
                    } else {
                        // Handle case where user is null, if necessary
                        dto.setEmail(null);
                        dto.setName(null);
                      //  dto.setSeekerId(null);
                    }

                    // Optionally set experience based on applicant data
                    dto.setExp("Senior"); // Replace this logic if needed
                    dto.setJob(null); // Set according to your requirements
                    dto.setUser(null); // Set according to your requirements

                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public ApplicantDto saveApplication(ApplicantDto applicantDto){

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Applicant applicant = ApplicantMapper.mapToApplicant(applicantDto);
        applicant.setUser(user);
        Applicant savedApplicant = applicantRepository.save(applicant);

        System.out.println("Application saved: " + savedApplicant );

        return ApplicantMapper.mapToApplicantDto(savedApplicant);


    }

    public Boolean updateStatusToPending (Integer jobseekerId , Long jobId ){

        Optional<User> user = userRepository.findById(jobseekerId);

        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }

        Optional<Job> job = jobRepository.findById(jobId);

        if(job.isEmpty()){
            throw new RuntimeException("Job not found");
        }


        Applicant applicant = applicantRepository.findByUserIdAndJobId(jobseekerId,jobId);

        if (applicant != null) {
            applicant.setStatus("pending"); // or the status you want to set
            applicantRepository.save(applicant);
            return true;
        }

        return true;


    }
}
