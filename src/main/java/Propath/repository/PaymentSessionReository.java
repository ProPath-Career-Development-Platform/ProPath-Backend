package Propath.repository;

import Propath.model.PaymentSession;
import Propath.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentSessionReository extends JpaRepository<PaymentSession,Long> {


    Optional<PaymentSession> findByUserAndSessionId(User user, String id);
    
    Optional<PaymentSession> findByUserAndUpgradeTrue(User user);

    Optional<PaymentSession> findByUserAndUpgradeFalse(User user);

    Optional<PaymentSession> findByUserAndMonthlyTrue(User user);
}
