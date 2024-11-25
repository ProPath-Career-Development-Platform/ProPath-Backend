package Propath.mapper;

import Propath.dto.BillingHistoryDto;
import Propath.dto.SubscriptionPlanDto;
import Propath.model.BillingHistory;
import Propath.model.SubscriptionPlan;

import java.time.LocalDateTime;

public class SubscriptionPlanMapper {
    public static SubscriptionPlanDto maptoSubscriptionPlanDto(SubscriptionPlan subscriptionPlan){
        return new SubscriptionPlanDto(
                subscriptionPlan.getId(),
                subscriptionPlan.getPlanName(),
                subscriptionPlan.getPrice(),
                subscriptionPlan.getCreatedAt(),
                subscriptionPlan.getIsFree()

        );
    }

    public static SubscriptionPlan maptoSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto){
        return new SubscriptionPlan(
                subscriptionPlanDto.getId(),
                subscriptionPlanDto.getPlanName(),
                subscriptionPlanDto.getPrice(),
                subscriptionPlanDto.getCreatedAt(),
                subscriptionPlanDto.getIsFree()

        );
    }
}
