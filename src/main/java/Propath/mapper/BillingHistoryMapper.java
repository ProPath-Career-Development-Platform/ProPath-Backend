package Propath.mapper;

import Propath.dto.BillingHistoryDto;
import Propath.model.BillingHistory;

public class BillingHistoryMapper {

    public static BillingHistoryDto maptoBillingHistoryDto(BillingHistory billingHistory){
        return new BillingHistoryDto(
                billingHistory.getId(),
                billingHistory.getUser(),
                billingHistory.getUserSubscription(),
                billingHistory.getAmountPaid(),
                billingHistory.getPaymentStatus(),
                billingHistory.getPaymentDate()

        );
    }

    public static BillingHistory maptoBillingHistory(BillingHistoryDto billingHistoryDto){
        return new BillingHistory(
                billingHistoryDto.getId(),
                billingHistoryDto.getUser(),
                billingHistoryDto.getUserSubscription(),
                billingHistoryDto.getAmountPaid(),
                billingHistoryDto.getPaymentStatus(),
                billingHistoryDto.getPaymentDate()

        );
    }
}
