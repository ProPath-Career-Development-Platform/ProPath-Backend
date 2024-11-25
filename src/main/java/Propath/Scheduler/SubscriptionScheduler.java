package Propath.Scheduler;

import Propath.model.Event;
import Propath.model.Job;
import Propath.model.UserSubscription;
import Propath.repository.EventRepository;
import Propath.repository.JobRepository;
import Propath.repository.UserSubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SubscriptionScheduler {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final JobRepository jobRepository;
    private final EventRepository eventRepository;

    public SubscriptionScheduler(
            UserSubscriptionRepository userSubscriptionRepository,
            JobRepository jobRepository,
            EventRepository eventRepository

    ) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.jobRepository = jobRepository;
        this.eventRepository = eventRepository;
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

    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
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

   // @Scheduled(cron = "0 * * * * *") // Executes every minute
    @Scheduled(cron = "0 0 0 * * *")
    public void expireEvents() {
        LocalDateTime now = LocalDateTime.now(); // Current date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // For "21:20" format

        List<Event> events = eventRepository.findByDeleteFalseAndStatus("active");

        System.out.println("expireEvents cron executed");

        for (Event event : events) {
            try {
                // Parse date and time
                LocalDate expiryDate = LocalDate.parse(event.getDate(), dateFormatter);
                LocalTime expiryTime = LocalTime.parse(event.getStartTime(), timeFormatter);

                // Combine date and time into LocalDateTime
                LocalDateTime expiryDateTime = LocalDateTime.of(expiryDate, expiryTime);

                // Check if the event has expired
                if (now.isAfter(expiryDateTime)) {
                    event.setStatus("expire");
                }
            } catch (Exception e) {
                System.err.println("Failed to parse expiry date or time for event ID: " + event.getId());
                e.printStackTrace();
            }
        }

        eventRepository.saveAll(events);
    }
}
