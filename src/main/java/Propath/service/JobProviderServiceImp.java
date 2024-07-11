package Propath.service;

import Propath.dto.JobProviderDto;
import Propath.mapper.JobProviderMapper;
import Propath.model.JobProvider;
import Propath.repository.JobProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobProviderServiceImp implements JobProviderService {

    private final JobProviderRepository jobProviderRepository;
    private final JobProviderMapper jobProviderMapper;

    @Autowired
    public JobProviderServiceImp(JobProviderRepository jobProviderRepository, JobProviderMapper jobProviderMapper) {
        this.jobProviderRepository = jobProviderRepository;
        this.jobProviderMapper = jobProviderMapper;
    }

    @Override
    public JobProviderDto saveJobProvider(JobProviderDto jobProviderDto) {
        JobProvider jobProvider = jobProviderMapper.maptoJobProvider(jobProviderDto);
        JobProvider savedJobProvider = jobProviderRepository.save(jobProvider);
        return jobProviderMapper.maptoJobProviderDto(savedJobProvider);
    }
}
