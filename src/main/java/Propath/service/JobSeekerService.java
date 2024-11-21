package Propath.service;

import Propath.dto.JobSeekerDto;

import java.util.List;

public interface JobSeekerService {

    public JobSeekerDto getJobSeekerDetails();

    List<JobSeekerDto> getJobSeekers();
}
