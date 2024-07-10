package Propath.mapper;

import Propath.dto.JobProviderDto;
import Propath.model.JobProvider;
import Propath.repository.JobProviderRepository;

public class JobProviderMapper {

    private JobProviderRepository jobProviderRepository;

    public static JobProviderDto maptoJobProviderDto(JobProvider jobProvider){
        return new JobProviderDto(
                jobProvider.getId(),
                jobProvider.getCompanyName()
        );
    }

    public static JobProvider maptoJobProvider(JobProviderDto jobProviderDto){
        return new JobProvider(
                jobProviderDto.getId(),
                jobProviderDto.getCompanyName()
        );
    }
}
