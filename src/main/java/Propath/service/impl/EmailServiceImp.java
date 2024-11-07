package Propath.service.impl;

import Propath.dto.VerficationTokenDto;
import Propath.mapper.VerificationTokenMapper;
import Propath.model.AuthenticationResponse;
import Propath.model.User;
import Propath.model.VerificationToken;
import Propath.repository.UserRepository;
import Propath.repository.VerficationRepository;
import Propath.service.EmailService;

import Propath.service.JwtService;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

@Service
public class EmailServiceImp implements EmailService {

    private final VerficationRepository verficationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Value("${mailersend.api.key}")
    private String apiKey;

    //@Allargsconstruct removed bcz -> creates a constructor with parameters for each field in the class, including apiKey
    @Autowired
    public EmailServiceImp(VerficationRepository verficationRepository, UserRepository userRepository, JwtService jwtService) {
        this.verficationRepository = verficationRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public String generateSecureRandomToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public LocalDateTime add15MinutesToCurrentTime() {
        return LocalDateTime.now().plus(15, ChronoUnit.MINUTES);
    }

    @Override
    public Boolean settingsEmailVerification(VerficationTokenDto verficationTokenDto) {

       // System.out.println("new email : " + verficationTokenDto.getNewEmail());

        //========= user info ========
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get(); // Get the User object

        Optional<VerificationToken> prevRequset = verficationRepository.findByUserId(user.getId());

        if(prevRequset.isPresent()){
            verficationRepository.delete(prevRequset.get());
        }

        // ========== token AND exp date ===============
        String token;
        Optional<VerificationToken> isTokenExist;
        do {
            token = this.generateSecureRandomToken(); // Assuming TokenUtil is the utility class
            isTokenExist = verficationRepository.findByToken(token);
        } while (isTokenExist.isPresent());

        // Set token and expiration time
        verficationTokenDto.setToken(token);
        verficationTokenDto.setExpirationTime(this.add15MinutesToCurrentTime()); // Assuming DateTimeUtil for expiry time

        // Save the token in the database
        VerificationToken tokenToSave = VerificationTokenMapper.maptoVeriicationToken(verficationTokenDto);
        tokenToSave.setUser(user); // Set the user here
        verficationRepository.save(tokenToSave);

        //================== email part =====================
        String newEmail = verficationTokenDto.getNewEmail();

        // Initialize MailerSend and set the API key
        MailerSend ms = new MailerSend();
        ms.setToken(apiKey); // Ensure apiKey is injected using @Value

        // Create email object and set "from" details
        Email email = new Email();
        email.setFrom("ProPath", "test@trial-z86org8v3yzlew13.mlsender.net");

        email.setSubject("Email Verification");

        // Use the user's email for sending the verification
        email.addRecipient(user.getUsername(), newEmail); // Use newEmail for recipient

        // Set the template ID from your MailerSend template
        email.setTemplateId("k68zxl2vq534j905");

        // Add personalized data
        email.addPersonalization("username", user.getUsername());
        email.addPersonalization("support_email", "test@trial-z86org8v3yzlew13.mlsender.net");
        email.addPersonalization("generated_token", token);

        // Send the email
        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println("Email sent successfully, Message ID: " + response.messageId);
            return true;
        } catch (MailerSendException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AuthenticationResponse checkVerification(String token) {
        // Get the currently authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Retrieve user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get(); // Get the User object

        // Retrieve the verification token associated with the user
        Optional<VerificationToken> tokenRow = verficationRepository.findByUserIdAndToken(user.getId(), token);

        if (tokenRow.isPresent()) {
            VerificationToken verificationToken = tokenRow.get();

            // Check if the token has expired
            if (LocalDateTime.now().isAfter(verificationToken.getExpirationTime())) {
                verficationRepository.delete(verificationToken);
                throw new RuntimeException("Token has expired");
            }

            // Update the user's email
            user.setEmail(verificationToken.getNewEmail());
            userRepository.save(user);

            // Delete the verification token after successful email update
            verficationRepository.delete(verificationToken);

            // Generate a new JWT with the updated email
            String newToken = jwtService.generateToken(user);

            // Return the new token in an AuthenticationResponse
            return new AuthenticationResponse(newToken);
        } else {
            // If token is not found, log the information for debugging
            System.out.println("Token not found for user ID: " + user.getId() + " and token: " + token);
            throw new RuntimeException("Invalid token");
        }
    }






}


