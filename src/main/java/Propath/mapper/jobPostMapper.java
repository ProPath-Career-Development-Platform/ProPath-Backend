package Propath.mapper;

import Propath.dto.PostJobDto;
import Propath.model.JobProvider;
import Propath.model.PostJobs;
import Propath.repository.JobProviderRepository;

public class jobPostMapper {
    private static JobProviderRepository jobProviderRepository;

    public jobPostMapper(JobProviderRepository jobProviderRepository) {
        jobPostMapper.jobProviderRepository = jobProviderRepository;
    }

    public static PostJobDto maptoPostJobsDto(PostJobs postJobs){
        return new PostJobDto(
                postJobs.getId(),
                postJobs.getJobProvider().getId(),
                postJobs.getJobTitle(),
                postJobs.getTags(),
                postJobs.getJobRole(),
                postJobs.getMinSalary(),
                postJobs.getMaxSalary(),
                postJobs.getSalaryType(),
                postJobs.getEducation(),
                postJobs.getExperience(),
                postJobs.getJobType(),
                postJobs.getJobLocation(),
                postJobs.getVacancies(),
                postJobs.getExpiryDate(),
                postJobs.getJobLevel(),
                postJobs.getJobDescription()
        );
    }

    public static PostJobs maptoPostJobs(PostJobDto postJobsDto){
        JobProvider jobProvider = jobProviderRepository.findById(postJobsDto.getJobProviderId()).orElseThrow(() -> new RuntimeException("JobProvider not found"));
        return new PostJobs(
                postJobsDto.getId(),
                jobProvider,
                postJobsDto.getJobTitle(),
                postJobsDto.getTags(),
                postJobsDto.getJobRole(),
                postJobsDto.getMinSalary(),
                postJobsDto.getMaxSalary(),
                postJobsDto.getSalaryType(),
                postJobsDto.getEducation(),
                postJobsDto.getExperience(),
                postJobsDto.getJobType(),
                postJobsDto.getJobLocation(),
                postJobsDto.getVacancies(),
                postJobsDto.getExpiryDate(),
                postJobsDto.getJobLevel(),
                postJobsDto.getJobDescription()
        );
    }
}