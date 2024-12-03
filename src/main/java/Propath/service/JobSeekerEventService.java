package Propath.service;

import Propath.dto.EventDto;
import Propath.dto.JobSeekerEventDto;

import java.util.List;

public interface JobSeekerEventService {

    static void upDateParticipant(JobSeekerEventDto event) {
    }

    public List<JobSeekerEventDto> getAllEvents();

    public JobSeekerEventDto registerEvent(Long eventId, JobSeekerEventDto jobseekerEvent);

    public List<EventDto> getFullEventDetails();

    public EventDto getEventById(long evenId);

    public JobSeekerEventDto getJobSeekerEventById(long eventId);

    JobSeekerEventDto getUserDetailsByTokenId(JobSeekerEventDto jobSeekerEventDto, Long eventId);


    void UpdateParticipantStatus(JobSeekerEventDto event);
}
