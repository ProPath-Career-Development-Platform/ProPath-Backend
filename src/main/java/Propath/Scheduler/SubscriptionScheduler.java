package Propath.Scheduler;

import Propath.model.Job;
import Propath.model.UserSubscription;
import Propath.repository.JobRepository;
import Propath.repository.UserSubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SubscriptionScheduler {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final JobRepository jobRepository;

    public SubscriptionScheduler(
            UserSubscriptionRepository userSubscriptionRepository,
            JobRepository jobRepository

    ) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.jobRepository = jobRepository;
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

    @Scheduled(cron = "0 * * * * *") // Executes every minute
    public void expireJobs() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;


        List<Job> jobs = jobRepository.findByStatus("active");

        System.out.println("expireJobs cron executed");

        for (Job job : jobs) {
            try {

                LocalDate expiryDate = LocalDate.parse(job.getExpiryDate(), dateFormatter);


                if (today.isAfter(expiryDate)) {
                    job.setStatus("expire");
                }
            } catch (Exception e) {
                System.err.println("Failed to parse expiry date for job ID: " + job.getId());
                e.printStackTrace();
            }
        }


        jobRepository.saveAll(jobs);
    }
}
