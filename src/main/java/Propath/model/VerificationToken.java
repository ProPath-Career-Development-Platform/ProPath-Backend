package Propath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.time.temporal.ChronoUnit;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expirationTime;
    private String newEmail;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;
}
