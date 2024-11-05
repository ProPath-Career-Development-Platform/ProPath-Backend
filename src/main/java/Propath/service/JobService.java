package Propath.service;

import Propath.dto.ApplicantDto;
import Propath.dto.CompanyDto;
import Propath.dto.JobDto;
import Propath.dto.PostJobDto;

import java.util.List;

public interface JobService {
    
    JobDto saveJob(JobDto jobDto);

    List<JobDto> getJobs();


    JobDto getJobById(Long id);

    Boolean setExpireJob(Long id);

    JobDto updateJob(Long id,JobDto jobDto);

    JobDto getPostJobById(Long postId);

    List<JobDto> getAllPostJobs();

    CompanyDto getCompanyInfoByJobId(Long id);

}
