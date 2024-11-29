package Propath.service;

import Propath.dto.ApplicantDto;
import Propath.dto.InterviewDto;
import Propath.model.Applicant;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ApplicantService {

    List<ApplicantDto> getApplicants(Long jobId);

    List<ApplicantDto> getApplicantsByUserIds(List<Integer> ids,Long jobId);

    ApplicantDto saveApplication(ApplicantDto applicantDto);

    Boolean updateStatusToPending(Integer seekerId, Long jobId);

    Boolean updateStatusToPreSelected(List<Integer> ids, Long jobId);

    Boolean updateStatusToSelected(List<Integer> ids, Long jobId);


    ApplicantDto getFormResponse(Long jobId, Integer userId);

    Boolean sendEmail(List<Integer>ids,Long jobId);

    Boolean checkUserAlreadyApplied(Integer userId, Long jobId);

    List<ApplicantDto> getAppliedJobsByUserId(Integer userId);


    List<ApplicantDto> getSelectedOrPreSelectedApplicants();

    List<InterviewDto> getInterviewsForSelectedOrPreSelectedApplicants();

}
