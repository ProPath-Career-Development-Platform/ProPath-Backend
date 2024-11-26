package Propath.service;

import Propath.dto.JobProviderDto;
import Propath.dto.VerficationTokenDto;
import Propath.model.AuthenticationResponse;

import java.time.LocalDateTime;

public interface EmailService {

    Boolean settingsEmailVerification(VerficationTokenDto verficationTokenDto);
    String generateSecureRandomToken();
    LocalDateTime add15MinutesToCurrentTime();
    AuthenticationResponse checkVerification(String token);

    void sendRegisterMailForJP(String name, String email);
    void sendEmails(String mail,String companyName,String title);
}
