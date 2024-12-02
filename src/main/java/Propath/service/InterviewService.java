package Propath.service;

import Propath.dto.InterviewDto;

import java.util.List;

public interface InterviewService {

    void createInterview(List<InterviewDto> interviewDto, Long jobId);

    List<InterviewDto> findInterviewsByJobIds(List<Integer> jobIds);

    List<InterviewDto> findInterviewsByJobId(Long jobId);

    InterviewDto updateInterview(Long interViewId, InterviewDto interviewDto);

    void updateInterviewStatus(Long interviewId, String status);
}
