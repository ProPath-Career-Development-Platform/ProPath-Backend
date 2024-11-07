package Propath.mapper;

import Propath.dto.JobDto;
import Propath.model.Job;
import Propath.model.JobProvider;
import Propath.repository.JobProviderRepository;
import Propath.repository.JobRepository;

import java.util.stream.Collectors;

public class JobMapper {

     private final JobRepository jobRepository;

     public JobMapper(JobRepository jobRepository){
         this.jobRepository = jobRepository;
     }

     public static JobDto maptoJobDto (Job job){
         return new JobDto(

                 job.getId(),
                 job.getJobTitle(),
                 job.getTags(),
                 job.getJobRole(),
                 job.getMinSalary(),
                 job.getMaxSalary(),
                 job.getSalaryType(),
                 job.getEducation(),
                 job.getExperience(),
                 job.getJobType(),
                 job.getVacancies(),
                 job.getExpiryDate(),
                 job.getJobLevel(),
                 job.getJobDescription(),
                 job.getCustomizedForm(),
                 job.getPostedIn(),
                 job.getDelete(),
                 job.getStatus(),
                 job.getApplicantCount(),
                 job.getUser(),
                 job.getCompany()

         );
     }

     public static Job maptoJob (JobDto jobDto){

         return new Job(

                 jobDto.getId(),
                 jobDto.getJobTitle(),
                 jobDto.getTags(),
                 jobDto.getJobRole(),
                 jobDto.getMinSalary(),
                 jobDto.getMaxSalary(),
                 jobDto.getSalaryType(),
                 jobDto.getEducation(),
                 jobDto.getExperience(),
                 jobDto.getJobType(),
                 jobDto.getVacancies(),
                 jobDto.getExpiryDate(),
                 jobDto.getJobLevel(),
                 jobDto.getJobDescription(),
                 jobDto.getCustomizedForm(),
                 jobDto.getPostedIn(),
                 jobDto.getDelete(),
                 jobDto.getStatus(),
                 jobDto.getApplicantCount(),
                 jobDto.getUser(),
                 jobDto.getCompany()

         );

     }

}
