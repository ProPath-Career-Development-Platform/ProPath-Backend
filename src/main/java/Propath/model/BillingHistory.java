package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "billing_history")
public class BillingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private UserSubscription userSubscription;

    @Column(nullable = false)
    private Double amountPaid;

    @Column(nullable = false, length = 20)
    private String paymentStatus; // E.g., SUCCESS, FAILED

    @Column(nullable = false, updatable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();
}
