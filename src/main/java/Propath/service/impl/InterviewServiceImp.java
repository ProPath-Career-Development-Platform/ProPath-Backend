package Propath.service.impl;

import Propath.dto.InterviewDto;
import Propath.exception.ResourceNotFoundException;
import Propath.mapper.ApplicantMapper;
import Propath.mapper.InterviewMapper;
import Propath.model.Interview;
import Propath.model.Job;
import Propath.model.User;
import Propath.repository.InterviewRepository;
import Propath.repository.JobRepository;
import Propath.repository.UserRepository;
import Propath.service.InterviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterviewServiceImp implements InterviewService {


    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createInterview(List<InterviewDto> interviewDtos, Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        for (InterviewDto dto : interviewDtos) {
            for (LocalTime timeSlot : dto.getTimeSlot()) { // assuming timeSlots is a List<LocalTime>
                Interview interview = InterviewMapper.mapToInterview(dto, timeSlot);
                interview.setJob(job);
                interview.setUser(null); // User will be null initially

                interviewRepository.save(interview);
            }
        }
    }

    @Override
    public List<InterviewDto> findInterviewsByJobIds(List<Integer> jobIds) {
        return interviewRepository.findByJobIdIn(jobIds)
                .stream()
                .map(interview -> {
                    InterviewDto dto = InterviewMapper.mapToInterviewDto(interview);
                    dto.setId(interview.getId());
                    dto.setDuration(interview.getDuration());
                    dto.setTimeSlot(Collections.singletonList(interview.getTimeSlot()));
                    dto.setInterviewDate(interview.getInterviewDate());

                    // Accessing related User entity data
                    if (interview.getUser() != null) {
//                        User user = new User();
//                        user.setEmail(interview.getUser().getEmail());
//                        user.setName(interview.getUser().getName());
                        dto.setUser(interview.getUser());
                    }

                    // Similarly, handle the Job entity if needed
                    if (interview.getJob() != null) {
//                        Job job = new Job();
//                        job.setId(interview.getJob().getId());
//                        job.setJobTitle(interview.getJob().getJobTitle());
                        dto.setJob(interview.getJob());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDto> findInterviewsByJobId(Long jobId) {
        return interviewRepository.findByJobId(jobId).stream()
                .map(InterviewMapper::mapToInterviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public InterviewDto updateInterview(Long interViewId, InterviewDto interviewDto) {
        Interview interview = interviewRepository.findById(interViewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        interview.setUser(userRepository.findById(interviewDto.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        interviewRepository.save(interview);

        return InterviewMapper.mapToInterviewDto(interview);
    }

    @Override
    public void updateInterviewStatus(Long interviewId, String status) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setStatus(status);
        interviewRepository.save(interview);

    }


}
