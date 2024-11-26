package Propath.dto;

import Propath.model.User;
import Propath.model.UserSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingHistoryDto {
    private Long id;
    private User user;
    private UserSubscription userSubscription;
    private Double amountPaid;
    private String paymentStatus; // E.g., SUCCESS, FAILED
    private LocalDateTime paymentDate = LocalDateTime.now();
}
