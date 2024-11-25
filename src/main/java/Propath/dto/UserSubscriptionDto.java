package Propath.dto;

import Propath.model.SubscriptionPlan;
import Propath.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionDto {

    private Long Id;
    private User user;
    private SubscriptionPlan subscriptionPlan;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status = "ACTIVE";
    private LocalDate createdAt = LocalDate.now();
    private String paidStatus = "NONE";
}
