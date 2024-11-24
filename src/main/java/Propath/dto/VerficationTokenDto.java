package Propath.dto;

import Propath.model.User;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerficationTokenDto {
    private Long id;
    private String token;
    private LocalDateTime expirationTime;
    private String newEmail;
    private User user;
}
