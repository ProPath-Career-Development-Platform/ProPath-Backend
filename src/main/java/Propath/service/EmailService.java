package Propath.service;

import Propath.dto.VerficationTokenDto;
import Propath.model.AuthenticationResponse;

import java.time.LocalDateTime;

public interface EmailService {

    Boolean settingsEmailVerification(VerficationTokenDto verficationTokenDto);
    String generateSecureRandomToken();
    LocalDateTime add15MinutesToCurrentTime();
    AuthenticationResponse checkVerification(String token);
}