package Propath.service.impl;

import Propath.dto.CompanyDto;
import Propath.dto.JobSeekerDto;
import Propath.mapper.CompanyMapper;
import Propath.mapper.JobSeekerMapper;
import Propath.model.Company;
import Propath.model.JobSeeker;
import Propath.model.User;
import Propath.repository.JobSeekerRepository;
import Propath.repository.UserRepository;
import Propath.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<JobSeekerDto> getJobSeekers() {
        List<JobSeeker> jobSeekers = jobSeekerRepository.findAll();
        return jobSeekers.stream().map((jobSeeker) -> JobSeekerMapper.mapToJobSeekerDto(jobSeeker)).collect(Collectors.toList());
    }
}
