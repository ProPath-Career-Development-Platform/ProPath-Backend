package Propath.service;

import Propath.dto.EventDto;
import Propath.dto.JobSeekerEventDto;
import Propath.model.Event;

import java.util.List;

public interface JobSeekerEventService {

    public List<JobSeekerEventDto> getAllEvents();

    public String registerEvent( Long eventId);

    public List<EventDto> getFullEventDetails();

    public EventDto getEventById(long evenId);

    public JobSeekerEventDto getJobSeekerEventById(long eventId);

 }
