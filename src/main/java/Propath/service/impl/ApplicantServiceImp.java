package Propath.service.impl;

import Propath.dto.ApplicantDto;
import Propath.dto.InterviewDto;
import Propath.mapper.ApplicantMapper;
import Propath.model.Applicant;
import Propath.model.Company;
import Propath.model.Job;
import Propath.model.User;
import Propath.repository.ApplicantRepository;
import Propath.repository.CompanyRepository;
import Propath.repository.JobRepository;
import Propath.repository.UserRepository;
import Propath.service.ApplicantService;
import Propath.service.EmailService;
import Propath.service.InterviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplicantServiceImp implements ApplicantService {

    private ApplicantRepository applicantRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;
    private CompanyRepository companyRepository;
    private EmailService emailService;
    private final InterviewService interviewService;

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

        if (job.getUser().getId() != (user.getId())) {
            throw new RuntimeException("Unauthorized request: user does not own this job.");
        }

        List<String> statuses = Arrays.asList("pending", "preSelected");
        List<Applicant> applicants = applicantRepository.findByJobIdAndStatusIn(jobId, statuses);


        return applicants.stream().map(ApplicantMapper::mapToApplicantDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ApplicantDto> getApplicantsByUserIds(List<Integer> userIds, Long jobId) {
        List<Applicant> applicants = applicantRepository.findByUserIdInAndJobId(userIds, jobId);

        return applicants.stream().map(ApplicantMapper::mapToApplicantDto)
                .collect(Collectors.toList());

    }


    @Override
    public ApplicantDto saveApplication(ApplicantDto applicantDto) {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Applicant applicant = ApplicantMapper.mapToApplicant(applicantDto);
        applicant.setUser(user);
        Applicant savedApplicant = applicantRepository.save(applicant);

        System.out.println("Application saved: " + savedApplicant);

        return ApplicantMapper.mapToApplicantDto(savedApplicant);


    }

    public Boolean updateStatusToPending(Integer jobseekerId, Long jobId) {

        Optional<User> user = userRepository.findById(jobseekerId);

        try {

            if (user.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            Optional<Job> job = jobRepository.findById(jobId);

            if (job.isEmpty()) {
                throw new RuntimeException("Job not found");
            }


            Applicant applicant = applicantRepository.findByUserIdAndJobId(jobseekerId, jobId)
                    .orElseThrow(() -> new RuntimeException("Application not found for userId: " + jobseekerId + " and jobId: " + jobId));


            applicant.setStatus("pending"); // or the status you want to set
            applicantRepository.save(applicant);
            return true;


        } catch (RuntimeException e) {
            return false;
        }

    }

    @Override
    public Boolean updateStatusToPreSelected(List<Integer> ids, Long jobId) {
        try {

            Optional<Job> job = jobRepository.findById(jobId);
            if (job.isEmpty()) {
                throw new RuntimeException("Job not found");
            }

            for (Integer id : ids) {

                Optional<User> user = userRepository.findById(id);
                if (user.isEmpty()) {
                    throw new RuntimeException("User with ID " + id + " not found");
                }


                Applicant applicant = applicantRepository.findByUserIdAndJobId(id, jobId)
                        .orElseThrow(() -> new RuntimeException("Application not found for userId: " + id + " and jobId: " + jobId));


                applicant.setStatus("preSelected");
                applicantRepository.save(applicant);
            }
            return true;

        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateStatusToSelected(List<Integer> ids, Long jobId) {

        try {

            Optional<Job> job = jobRepository.findById(jobId);
            if (job.isEmpty()) {
                throw new RuntimeException("Job not found");
            }

            for (Integer id : ids) {

                Optional<User> user = userRepository.findById(id);
                if (user.isEmpty()) {
                    throw new RuntimeException("User with ID " + id + " not found");
                }


                Applicant applicant = applicantRepository.findByUserIdAndJobId(id, jobId)
                        .orElseThrow(() -> new RuntimeException("Application not found for userId: " + id + " and jobId: " + jobId));


                applicant.setStatus("selected");
                applicantRepository.save(applicant);
            }
            return true;

        } catch (RuntimeException e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
//    public ApplicantDto getFormResponse(Long jobId, Integer userId) {
//        return null;
//    }


    public ApplicantDto getFormResponse(Long jobId, Integer UserId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<User> jobSeeker = userRepository.findById(UserId);

        if (jobSeeker.isEmpty()) {
            throw new RuntimeException("job seeker id not own");

        }

        Optional<Applicant> application = applicantRepository.findByUserIdAndJobId(UserId, jobId);

        if (application.isEmpty()) {
            return null;
        } else {
            return ApplicantMapper.mapToApplicantDto(application.get());
        }


    }

    public Boolean sendEmail(List<Integer> ids,Long jobId) {
        try {

            Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("job is not found"));

            String title = job.getJobTitle();
            User user = job.getUser();
            String companyName;
            try {

                Optional<Company> optionalCompany = companyRepository.findByUserId(user.getId());

                // Get the company from the Optional
                Company company = optionalCompany.orElseThrow(() ->
                        new RuntimeException("Company not found for the user"));
                companyName = company.getCompanyName();
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving the company", e);
            }


            for (Integer id : ids) {
                Applicant applicant = applicantRepository.findByUserId(id);

                if (applicant == null) {
                    throw new RuntimeException("Applicant not found for the id" + id);
                }
                String mail = applicant.getEmail();


//                String subject = "Interview Invitation for " + title;
//                String body = "Dear " + name + ",\n\n"
//                        + "You have been selected for an interview for the position of " + title + " at " + companyName + ".\n"
//                        + "Please check your schedule and prepare accordingly.\n\n"
//                        + "Best regards,\n" + companyName;

                System.out.println(companyName + "," + mail + "," + title);
                emailService.sendEmails(mail, companyName, title);
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error while sending emails", e);
        }
    }

            @Override
            public Boolean checkUserAlreadyApplied (Integer userId, Long jobId){
                return applicantRepository.findByUserIdAndJobId(userId, jobId).isPresent();
            }

            @Override
            public List<ApplicantDto> getAppliedJobsByUserId (Integer userId){
                // Use findAllByUserIdIn with a single user ID in a list
                List<Applicant> applicants = applicantRepository.findAllByUserIdIn(Collections.singletonList(userId));
                return applicants.stream()
                        .map(ApplicantMapper::mapToApplicantDto)
                        .collect(Collectors.toList());
            }

    @Override
    public List<ApplicantDto> getSelectedOrPreSelectedApplicants() {
        List<String> statuses = Arrays.asList("preSelected", "selected");
        List<Applicant> applicants = applicantRepository.findByStatusIn(statuses);
        return applicants.stream()
                .map(ApplicantMapper::mapToApplicantDto)
                .collect(Collectors.toList());
    }

    public List<InterviewDto> getInterviewsForSelectedOrPreSelectedApplicants() {
        List<String> statuses = Arrays.asList("preSelected", "selected");
        List<Applicant> applicants = applicantRepository.findByStatusIn(statuses);
        List<Long> jobIds = applicants.stream()
                .map(applicant -> applicant.getJob().getId())
                .distinct()
                .collect(Collectors.toList());
        return jobIds.stream()
                .flatMap(jobId -> interviewService.findInterviewsByJobId(jobId).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void updateApplicantStatus(Integer userId, Long jobId, String status) {
        Applicant applicant = applicantRepository.findByJobIdAndUserId(jobId, userId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        applicant.setStatus(status);

        String rest_body;

        if ("HIRED".equals(status)) {
            rest_body = "We are thrilled to inform you that you have been hired for the job role you applied for! "
                    + "Our team is excited to welcome you onboard. Please keep an eye on your inbox for further instructions and onboarding details, "
                    + "which we will share soon.";
        } else {
            rest_body = "After careful consideration of all candidates, we regret to inform you that your application has not been successful. "
                    + "This decision was not an easy one, as we received many highly qualified applications. "
                    + "We truly value your effort and encourage you to apply for other opportunities with us in the future.";
        }

        User user = applicant.getJob().getUser();
        String companyName;
        try {

            Optional<Company> optionalCompany = companyRepository.findByUserId(user.getId());

            // Get the company from the Optional
            Company company = optionalCompany.orElseThrow(() ->
                    new RuntimeException("Company not found for the user"));
            companyName = company.getCompanyName();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving the company", e);
        }

        System.out.println(applicant.getJob().getJobTitle());
        System.out.println(applicant.getEmail());
        System.out.println(companyName);
        System.out.println(applicant.getUser().getName());
        System.out.println(rest_body);



        emailService.sendStatusMail(
                applicant.getJob().getJobTitle(),
                applicant.getEmail(),
                companyName,
                applicant.getUser().getName(),
                rest_body
        );

        applicantRepository.save(applicant);
    }


}

//    @Override
//
//    public ApplicantDto getFormResponse(Long jobId, Integer UserId){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName(); // Assuming you store the email in the principal
//
//        // Find the user by email
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Optional<User> jobSeeker = userRepository.findById(UserId);
//
//        if(jobSeeker.isEmpty()){
//            throw new RuntimeException("job seeker id not own");
//
//        }
//
//        Optional<Applicant> application = applicantRepository.findByUserIdAndJobId(UserId,jobId);
//
//        if(application.isEmpty()){
//            return null;
//        }else{
//            return ApplicantMapper.mapToApplicantDto(application.get());
//        }
//
//
//        public Boolean sendEmail(List<Integer> ids,Long jobId) {
//            try {
//
//                Job job = jobRepository.findById(jobId).orElseThrow(()->new RuntimeException("job is not found"));
//
//                String title= job.getJobTitle();
//                User user = job.getUser();
//                String companyName;
//                try{
//
//                    Optional<Company> optionalCompany = companyRepository.findByUserId(user.getId());
//
//                    // Get the company from the Optional
//                    Company company = optionalCompany.orElseThrow(() ->
//                            new RuntimeException("Company not found for the user"));
//                    companyName = company.getCompanyName();
//                }catch(Exception e){
//                    throw new RuntimeException("Error retrieving the company", e);
//                }
//
//
//                for(Integer id:ids){
//                    Applicant applicant = applicantRepository.findByUserId(id);
//
//                    if(applicant==null){
//                        throw new RuntimeException("Applicant not found for the id"+id);
//                    }
//                    String mail = applicant.getEmail();
//                    String name = applicant.getName();
//
//                    String subject = "Interview Invitation for " + title;
//                    String body = "Dear " + name + ",\n\n"
//                            + "You have been selected for an interview for the position of " + title + " at " + companyName + ".\n"
//                            + "Please check your schedule and prepare accordingly.\n\n"
//                            + "Best regards,\n" + companyName;
//
//
//                    emailService.sendEmails(mail,subject,body);
//                }
//
//                return true;
//            } catch (Exception e) {
//                throw new RuntimeException("Error while sending emails",e);
//
//            }
//
//
//
//        }
//    }

