package Propath.service;

import Propath.dto.JobDto;

import java.util.List;

public interface JobService {
    
    JobDto saveJob(JobDto jobDto);

    List<JobDto> getJobs();


    JobDto getJobById(Long id);
}
