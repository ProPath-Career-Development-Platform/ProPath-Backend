package Propath.mapper;

import Propath.dto.FavoritesJobsDto;
import Propath.model.FavoriteJobs;
import org.springframework.stereotype.Component;

@Component
public class FavoritesJobsMapper {
    public FavoritesJobsDto mapToFavoritesJobsDto(FavoriteJobs favoriteJobs) {
        return new FavoritesJobsDto(
                favoriteJobs.getId(),
                favoriteJobs.getJobId(),
                favoriteJobs.getCompanyId(),
                favoriteJobs.getUserId()
        );
    }

    public static FavoriteJobs mapToFavoritesJobs(FavoritesJobsDto favoritesJobsDto) {
        return new FavoriteJobs(
                favoritesJobsDto.getId(),
                favoritesJobsDto.getJobId(),
                favoritesJobsDto.getCompanyId(),
                favoritesJobsDto.getUserId()
        );
    }
}
