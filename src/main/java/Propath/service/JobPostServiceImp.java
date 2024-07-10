package Propath.service;

import Propath.dto.PostJobDto;
import Propath.mapper.jobPostMapper;
import Propath.model.PostJobs;
import Propath.repository.JobPostRepository;
import Propath.repository.JobProviderRepository;
import org.springframework.stereotype.Service;

@Service
public class JobPostServiceImp implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final JobProviderRepository jobProviderRepository;

    public JobPostServiceImp(JobPostRepository jobPostRepository, JobProviderRepository jobProviderRepository) {
        this.jobPostRepository = jobPostRepository;
        this.jobProviderRepository = jobProviderRepository;
        new jobPostMapper(jobProviderRepository); // Initialize the mapper with the repository
    }

    @Override
    public PostJobDto savePostJob(PostJobDto postJobDto) {
        PostJobs postJobs = jobPostMapper.maptoPostJobs(postJobDto);
        PostJobs savedJobs = jobPostRepository.save(postJobs);
        return jobPostMapper.maptoPostJobsDto(savedJobs);
    }
}