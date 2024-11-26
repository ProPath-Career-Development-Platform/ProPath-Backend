package Propath.mapper;

import Propath.dto.UserSubscriptionDto;
import Propath.model.UserSubscription;

public class UserSubscriptionMapper {

    public static UserSubscriptionDto maptoUserSubcriptionDto(UserSubscription userSubscription){
        return new UserSubscriptionDto(
                userSubscription.getId(),
                userSubscription.getUser(),
                userSubscription.getSubscriptionPlan(),
                userSubscription.getStartDate(),
                userSubscription.getEndDate(),
                userSubscription.getStatus(),
                userSubscription.getCreatedAt(),
                userSubscription.getPaidStatus()
        );
    }

    public static UserSubscription maptoUserSubcription(UserSubscriptionDto userSubscriptionDto){
        return new UserSubscription(
                userSubscriptionDto.getId(),
                userSubscriptionDto.getUser(),
                userSubscriptionDto.getSubscriptionPlan(),
                userSubscriptionDto.getStartDate(),
                userSubscriptionDto.getEndDate(),
                userSubscriptionDto.getStatus(),
                userSubscriptionDto.getCreatedAt(),
                userSubscriptionDto.getPaidStatus()
        );
    }
}
