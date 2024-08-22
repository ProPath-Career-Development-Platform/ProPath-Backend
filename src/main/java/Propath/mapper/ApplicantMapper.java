package Propath.mapper;

import Propath.dto.ApplicantDto;
import Propath.model.Applicant;
import Propath.model.PostJobs;
import Propath.model.User;
import Propath.repository.JobPostRepository;
import Propath.repository.UserRepository;

public class ApplicantMapper {

    private final JobPostRepository jobpostRepository;
    private final UserRepository userRepository;

    public ApplicantMapper(JobPostRepository jobpostRepository, UserRepository userRepository) {
        this.jobpostRepository = jobpostRepository;
        this.userRepository = userRepository;
    }

    public static ApplicantDto mapToApplicantDto(Applicant applicant) {
        return new ApplicantDto(
                applicant.getApplicantId(),
                applicant.getApplicantName(),
                applicant.getEmail(),
                applicant.getATS_Score(),
                applicant.getAppliedDate(),
                applicant.getExpLevel(),
                applicant.getPostJobs().getId(),
                applicant.getUser().getId()
        );
    }

    public static Applicant mapToApplicant(ApplicantDto applicantDto, JobPostRepository jobpostRepository, UserRepository userRepository) {
        PostJobs postJobs = jobpostRepository.findById((Integer) applicantDto.getJobId()).orElse(null);
        User user = userRepository.findById((Integer) applicantDto.getJobSeekerId()).orElse(null);

        return new Applicant(
                applicantDto.getApplicantId(),
                applicantDto.getEmail(),
                applicantDto.getApplicantName(),
                applicantDto.getATS_Score(),
                applicantDto.getExpLevel(),
                applicantDto.getAppliedDate(),
                postJobs,
                user
        );
    }


}

