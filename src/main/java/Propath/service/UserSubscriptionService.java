package Propath.service;

import Propath.dto.SubscriptionPlanDto;
import Propath.dto.UserSubscriptionDto;
import java.util.List;

public interface UserSubscriptionService {
    void createBasicUser(int id);

    UserSubscriptionDto getSubscription();

    List<SubscriptionPlanDto> getSubscriptionPlans();

    Boolean isPlanExpired();
}
