package Propath.service.impl;

import Propath.dto.JobSeekerDto;
import Propath.mapper.JobSeekerMapper;
import Propath.model.JobSeeker;
import Propath.model.User;
import Propath.repository.JobSeekerRepository;
import Propath.repository.UserRepository;
import Propath.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerImp implements JobSeekerService {
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public JobSeekerDto getJobSeekerDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new RuntimeException("Email Not found"));

        JobSeeker job = new JobSeeker();
        job.setUser(user);
        return JobSeekerMapper.mapToJobSeekerDto(job);
    }
}
