package Propath.service;

import Propath.dto.JobSeekerEventDto;

import java.util.List;

public interface JobSeekerEventService {

    public List<JobSeekerEventDto> getAllEvents();

    public String registerEvent(int seekerId , Long eventId);
}
