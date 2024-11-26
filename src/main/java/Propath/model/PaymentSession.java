package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private Boolean upgrade;
    private Boolean monthly;
    private LocalDateTime createdAt;
    private double price;
    @ManyToOne
    @JoinColumn(name = "planId")
    private SubscriptionPlan subscriptionPlan;
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

}
