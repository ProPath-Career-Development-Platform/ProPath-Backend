package Propath.repository;

import Propath.model.FavoriteJobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesJobs extends JpaRepository<FavoriteJobs, Long> {
    Optional<FavoriteJobs> findByUserIdAndJobIdAndCompanyId(Long userId, Long jobId, Long companyId);

    List<FavoriteJobs> findByUserId(Long userId);
}
