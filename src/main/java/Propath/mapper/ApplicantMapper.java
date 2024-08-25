package Propath.mapper;

import Propath.dto.ApplicantDto;
import Propath.model.Applicant;
import Propath.repository.ApplicantRepository;

public class ApplicantMapper {


    private final ApplicantRepository applicantRepository;

    public ApplicantMapper(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public static ApplicantDto mapToApplicantDto(Applicant applicant) {
        return new ApplicantDto(
                applicant.getId(),
                applicant.getAtsScore(),
                applicant.getAppliedDate(),
                applicant.getStatus(),
                applicant.getResponse(),
                applicant.getEmail(),
                applicant.getExp(),
                applicant.getName(),
                applicant.getSeekerId(),
                applicant.getJob(),
                applicant.getUser()

        );
    }

    public static Applicant mapToApplicant(ApplicantDto applicantDto) {


        return new Applicant(
                applicantDto.getId(),
                applicantDto.getAtsScore(),
                applicantDto.getAppliedDate(),
                applicantDto.getStatus(),
                applicantDto.getResponse(),
                applicantDto.getEmail(),
                applicantDto.getExp(),
                applicantDto.getName(),
                applicantDto.getSeekerId(),
                applicantDto.getJob(),
                applicantDto.getUser()

        );
    }


}

