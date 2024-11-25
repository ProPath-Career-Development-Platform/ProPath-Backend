package Propath.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanDto{

    private Long id;
    private String planName;
    private Double price;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Boolean isFree = false;
}
