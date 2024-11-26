package Propath.repository;

import Propath.dto.UserSubscriptionDto;
import Propath.model.User;
import Propath.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {


    Optional<UserSubscription> findByUser_IdAndStatus(int id, String active);

    Optional<UserSubscription> findByUser_IdAndStatusAndPaidStatus(int id, String active, String none);

    List<UserSubscription> findByStatusAndEndDateBeforeAndPaidStatus(String active, LocalDate today, String success);
}
