package Propath.Scheduler;

import Propath.model.UserSubscription;
import Propath.repository.UserSubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionScheduler {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public SubscriptionScheduler(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") //mid night
    //@Scheduled(cron = "0 * * * * *") //every 1 min
    public void subscriptionExpire(){

        LocalDate today = LocalDate.now();

        List<UserSubscription> subscriptions = userSubscriptionRepository.findByStatusAndEndDateBeforeAndPaidStatus("ACTIVE", today,"success");

        System.out.println("Subscription scheduler executed");

        for(UserSubscription sub : subscriptions){

            if (today.isAfter(sub.getEndDate())) {

                //sub.setStatus("EXPIRE");
                sub.setPaidStatus("none");

                userSubscriptionRepository.save(sub);
            }
        }


    }
}
