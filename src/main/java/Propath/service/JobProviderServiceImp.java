package Propath.service;

import Propath.dto.JobProviderDto;
import Propath.mapper.JobProviderMapper;
import Propath.model.JobProvider;
import Propath.repository.JobPostRepository;
import Propath.repository.JobProviderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobProviderServiceImp implements JobProviderService{

    private JobProviderRepository jobP;
    @Override
    public JobProviderDto saveJobProvider(JobProviderDto jobProviderDto) {
        JobProvider jobProvider = JobProviderMapper.maptoJobProvider(jobProviderDto);
        JobProvider savedJobProvider = jobP.save(jobProvider);
        return JobProviderMapper.maptoJobProviderDto(savedJobProvider);
    }
}
