package Propath.service.impl;

import Propath.dto.SubscriptionPlanDto;
import Propath.dto.UserSubscriptionDto;
import Propath.mapper.SubscriptionPlanMapper;
import Propath.mapper.UserSubscriptionMapper;
import Propath.model.SubscriptionPlan;
import Propath.model.User;
import Propath.model.UserSubscription;
import Propath.repository.SubscriptionPlanRepository;
import Propath.repository.UserRepository;
import Propath.repository.UserSubscriptionRepository;
import Propath.service.UserSubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserSubscriptionServiceImp implements UserSubscriptionService {

    private UserRepository userRepository;
    private SubscriptionPlanRepository subscriptionPlanRepository;
    private UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public void createBasicUser(int id){



        // Find the user by email
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<SubscriptionPlan> subPlan = subscriptionPlanRepository.findById(1L);

        UserSubscription userSubscription = new UserSubscription();

        userSubscription.setUser(userOptional.get());
        userSubscription.setSubscriptionPlan(subPlan.get());
        userSubscription.setStartDate(LocalDate.now());
        userSubscription.setEndDate(null);

        userSubscriptionRepository.save(userSubscription);


    }

    @Override
    public UserSubscriptionDto getSubscription(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<UserSubscription> subscription = userSubscriptionRepository.findByUser_IdAndStatus(userOptional.get().getId(),"ACTIVE");

        if(subscription.isEmpty()){
            throw new RuntimeException("Plan not found");
        }else{
            return UserSubscriptionMapper.maptoUserSubcriptionDto(subscription.get());
        }
    }

    @Override
    public List<SubscriptionPlanDto> getSubscriptionPlans(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        List<SubscriptionPlan> plans = subscriptionPlanRepository.findAll();

        return plans.stream().map(SubscriptionPlanMapper::maptoSubscriptionPlanDto).collect(Collectors.toList());


    }

    @Override
    public Boolean isPlanExpired() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found for email: " + userEmail);
        }

        Optional<UserSubscription> planOptional = userSubscriptionRepository.findByUser_IdAndStatus(userOptional.get().getId(), "ACTIVE");

        if (planOptional.isEmpty()) {
            throw new RuntimeException("No active subscription found for user: " + userEmail);
        }

        UserSubscription plan = planOptional.get();
        String planName = plan.getSubscriptionPlan().getPlanName();
        LocalDate endDate = plan.getEndDate();

        if ("BASIC".equals(planName)) {
            return false; // BASIC plan is not considered expired
        }

        if (("STANDARD".equals(planName) || "PREMIUM".equals(planName)) && endDate.isAfter(LocalDate.now())) {
            return false; // STANDARD or PREMIUM and still valid
        }

        // Plan is expired
        return true;
    }

}
