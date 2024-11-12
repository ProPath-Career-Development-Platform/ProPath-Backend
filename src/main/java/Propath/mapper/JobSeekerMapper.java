package Propath.mapper;

import Propath.dto.JobSeekerDto;
import Propath.model.Job;
import Propath.model.JobSeeker;
import Propath.model.User;
import Propath.repository.JobSeekerRepository;
import Propath.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class JobSeekerMapper {

    private  JobSeekerRepository jobSeekerRepository;
    public JobSeekerMapper(JobSeekerRepository jobSeekerRepository){this.jobSeekerRepository = jobSeekerRepository;}
    public static JobSeekerDto mapToJobSeekerDto(JobSeeker jobSeeker){
        return new JobSeekerDto(
                jobSeeker.getId(),
            jobSeeker.getProfilePicture(),
            jobSeeker.getUser()
        );
    }

    public static JobSeeker mapToJobSeeker(JobSeekerDto jobSeekerDto) {
       return new JobSeeker(
               jobSeekerDto.getId(),
               jobSeekerDto.getProfilePicture(),
               jobSeekerDto.getUser()
       );

    }


}
