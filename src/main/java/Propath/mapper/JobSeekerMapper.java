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
                jobSeeker.getUser(),
                jobSeeker.getName(),
                jobSeeker.getPreferred_classification(),
                jobSeeker.getPreferred_sub_classification(),
                jobSeeker.getLocation(),
                jobSeeker.getContact_no(),
                jobSeeker.getEmail(),
                jobSeeker.getGender()
        );
    }

    public static JobSeeker mapToJobSeeker(JobSeekerDto jobSeekerDto) {
       return new JobSeeker(
               jobSeekerDto.getId(),
               jobSeekerDto.getProfilePicture(),
               jobSeekerDto.getUser(),
               jobSeekerDto.getName(),
               jobSeekerDto.getPreferred_classification(),
               jobSeekerDto.getPreferred_sub_classification(),
               jobSeekerDto.getLocation(),
               jobSeekerDto.getContact_no(),
               jobSeekerDto.getEmail(),
               jobSeekerDto.getGender()
       );

    }


}
