package Propath.service.impl;

import Propath.dto.FavoritesJobsDto;
import Propath.mapper.FavoritesJobsMapper;
import Propath.model.FavoriteJobs;
import Propath.model.User;
import Propath.repository.FavoritesJobs;
import Propath.repository.UserRepository;
import Propath.service.FavoritesJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritesJobsServiceImp implements FavoritesJobsService {

    @Autowired
    private FavoritesJobs favoritesJobsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveFavoritesJobs(Long jobId, Long companyId, Long userId) {
        // Validate user existence
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Create and save favorite job entry
        FavoriteJobs favoriteJobs = new FavoriteJobs();
        favoriteJobs.setUserId((long) user.getId());
        favoriteJobs.setJobId(jobId);
        favoriteJobs.setCompanyId(companyId);

        favoritesJobsRepository.save(favoriteJobs);
    }


    @Override
    public void removeFavoriteJob(Long jobId, Long companyId, Long userId) {
        // Validate that the user exists
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Fetch the favorite job entry
        FavoriteJobs favoriteJob = favoritesJobsRepository.findByUserIdAndJobIdAndCompanyId(userId, jobId, companyId)
                .orElseThrow(() -> new RuntimeException("Error: Favorite job not found."));

        // Delete the favorite job
        favoritesJobsRepository.delete(favoriteJob);
    }


    @Override
    public List<FavoritesJobsDto> getFavoriteJobs(Long userId) {
        // Validate that the user exists
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Retrieve favorite jobs for the user
        List<FavoriteJobs> favoriteJobsList = favoritesJobsRepository.findByUserId(userId);

        // Convert entity list to DTO list
        return favoriteJobsList.stream()
                .map(favJob -> new FavoritesJobsDto(
                        favJob.getId(),
                        favJob.getJobId(),
                        favJob.getCompanyId(),
                        favJob.getUserId()))
                .collect(Collectors.toList());
    }

}