package Propath.repository;

import Propath.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerficationRepository extends JpaRepository<VerificationToken,Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUserId(int id);
    Optional<VerificationToken> findByUserIdAndToken(int id, String token);
}
