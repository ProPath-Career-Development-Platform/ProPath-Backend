package Propath.service.impl;

import Propath.dto.SubscriptionPlanDto;
import Propath.dto.UserSubscriptionDto;
import Propath.mapper.EventMapper;
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
    public void createBasicUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

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
}
