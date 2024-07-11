package Propath.mapper;

import Propath.dto.JobProviderDto;
import Propath.model.JobProvider;
import Propath.model.User;
import Propath.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class JobProviderMapper {

    private final UserRepository userRepository;

    public JobProviderMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JobProviderDto maptoJobProviderDto(JobProvider jobProvider) {
        return new JobProviderDto(
                jobProvider.getId(),
                jobProvider.getUser().getId(),
                jobProvider.getCompanyName()
        );
    }

    public JobProvider maptoJobProvider(JobProviderDto jobProviderDto) {
        return userRepository.findById(jobProviderDto.getUserId())
                .map(user -> new JobProvider(
                        jobProviderDto.getId(),
                        user,
                        jobProviderDto.getCompanyName()))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + jobProviderDto.getUserId()));
    }
}
