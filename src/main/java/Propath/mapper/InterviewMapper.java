package Propath.mapper;

import Propath.dto.InterviewDto;

import Propath.model.Interview;
import Propath.repository.InterviewRepository;

import java.time.LocalTime;
import java.util.List;

public class InterviewMapper {

    private final InterviewRepository interviewRepository;

    public InterviewMapper(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public static InterviewDto mapToInterviewDto(Object interview) {
        if (interview instanceof Interview) {
            Interview interviewEntity = (Interview) interview;
            return new InterviewDto(
                    interviewEntity.getId(),
                    interviewEntity.getDuration(),
                    List.of(interviewEntity.getTimeSlot()),
                    interviewEntity.getInterviewDate(),
                    interviewEntity.getUser(),
                    interviewEntity.getJob()
            );
        } else {
            // Handle the case where the object is not an Interview
            throw new IllegalArgumentException("Object is not an instance of Interview");
        }
    }

//    public static Interview mapToInterview(InterviewDto interviewDto) {
//
//
//        return new Interview(
//                interviewDto.getId(),
//                interviewDto.getInterviewDate(),
//                interviewDto.getDuration(),
//                interviewDto.getTimeSlot(),
//                interviewDto.getJob(),
//                interviewDto.getUser()
//        );
//    }

    public static Interview mapToInterview(InterviewDto interviewDto, LocalTime timeSlot) {
        return new Interview(
                interviewDto.getId(),
                interviewDto.getInterviewDate(),
                interviewDto.getDuration(),
                timeSlot,
                interviewDto.getJob(),
                interviewDto.getUser()
        );
    }


}
