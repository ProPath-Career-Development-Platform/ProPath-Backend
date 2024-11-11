package Propath.service;

import Propath.dto.JobProviderDto;
import Propath.mapper.JobProviderMapper;
import Propath.model.JobProvider;
import Propath.model.User;
import Propath.repository.JobProviderRepository;
import Propath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobProviderServiceImp implements JobProviderService {

    private final JobProviderRepository jobProviderRepository;
    private final JobProviderMapper jobProviderMapper;
    private final UserRepository userRepository;

    @Autowired
    public JobProviderServiceImp(JobProviderRepository jobProviderRepository, JobProviderMapper jobProviderMapper,UserRepository userRepository) {
        this.jobProviderRepository = jobProviderRepository;
        this.jobProviderMapper = jobProviderMapper;
        this.userRepository = userRepository;
    }

    @Override
    public JobProviderDto saveJobProvider(JobProviderDto jobProviderDto) {
        JobProvider jobProvider = jobProviderMapper.maptoJobProvider(jobProviderDto);
        JobProvider savedJobProvider = jobProviderRepository.save(jobProvider);
        return jobProviderMapper.maptoJobProviderDto(savedJobProvider);
    }

    @Override
    public Boolean updatePersonalName(User user) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();


            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return false;
            }


            User userlogged = userOptional.get();
            userlogged.setName(user.getUsername());

            // Save the updated user object in the repository
            userRepository.save(userlogged);


            return true;

        } catch (Exception e) {
            // Log the exception (optional)
            e.printStackTrace();

            // Return false if an exception occurs
            return false;
        }
    }

}
