package Propath.service;

import Propath.dto.*;
import Propath.model.Job;

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

    List<Integer> findJobIdsByProviderId(int userId);

    public JobDto getJobByIdJs(Long id);

    List<JobDto> getAllJobs();

    List<JobDto> getAllPostedJobs();

    List<JobDto> getRelatedJobsByTags(Long jobId);

}
