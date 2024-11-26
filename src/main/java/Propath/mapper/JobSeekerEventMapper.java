package Propath.mapper;

import Propath.dto.JobSeekerDto;
import Propath.dto.JobSeekerEventDto;
import Propath.model.JobseekerEvent;
import Propath.repository.JobSeekerEventRepository;



public class JobSeekerEventMapper {

    private JobSeekerEventRepository jobSeekerEventRepository;

    public static JobSeekerEventDto maptoJobSeekerEventDto(JobseekerEvent jobSeekerEvent){
        return new JobSeekerEventDto(
                jobSeekerEvent.getRegistrationId(),
                jobSeekerEvent.getEvent(),
                jobSeekerEvent.getJobSeeker(),
                jobSeekerEvent.getAppliedDate(),
                jobSeekerEvent.getIsApplied()
        );
    }

    public static JobseekerEvent maptoJobSeekerEvent(JobSeekerEventDto jobSeekerEventDto){

        return new JobseekerEvent(
                jobSeekerEventDto.getId(),
                jobSeekerEventDto.getEvent(),
                jobSeekerEventDto.getJobSeeker(),
                jobSeekerEventDto.getAppliedDate(),
                jobSeekerEventDto.getIsApplied()
        );
    }

}
