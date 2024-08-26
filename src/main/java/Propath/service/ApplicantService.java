package Propath.service;

import Propath.dto.ApplicantDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ApplicantService {

    List<ApplicantDto> getApplicants(Long jobId);

    List<ApplicantDto> getApplicantsByUserIds(List<Integer> ids);

    ApplicantDto saveApplication(ApplicantDto applicantDto);

    Boolean updateStatusToPending(Integer seekerId, Long jobId);
}
