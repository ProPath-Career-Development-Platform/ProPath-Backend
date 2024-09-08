package Propath.service;

import Propath.dto.ApplicantDto;
import Propath.dto.JobDto;

import java.util.List;

public interface JobService {




    JobDto saveJob(JobDto jobDto);

    List<JobDto> getJobs();


    JobDto getJobById(Long id);

    Boolean setExpireJob(Long id);


    JobDto updateJob(Long id,JobDto jobDto);

    List<Integer> findJobIdsByProviderId(int userId);

}
