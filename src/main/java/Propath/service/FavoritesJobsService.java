package Propath.service;

import Propath.dto.FavoritesJobsDto;

import java.util.List;

public interface FavoritesJobsService {
    void saveFavoritesJobs(Long jobId, Long companyId, Long userId);
    void removeFavoriteJob(Long jobId, Long companyId, Long userId);

    List<FavoritesJobsDto> getFavoriteJobs(Long userId);

    Boolean isFavorite(Long jobId, Long companyId, Long userId);
}
