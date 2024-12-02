package Propath.service;

import Propath.dto.UpdateProfileRequest;
import Propath.model.AuthenticationResponse;
import Propath.model.User;
import Propath.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private  final UserSubscriptionService userSubscriptionService;


    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager,EmailService emailService, UserSubscriptionService userSubscriptionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.userSubscriptionService = userSubscriptionService;

    }

    public AuthenticationResponse register(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = userRepository.save(user);

        String Role = String.valueOf(request.getRole());

        Optional<User> newUser = userRepository.findByEmail(request.getEmail());

        if(Role.equals("JobProvider")){

            //subscription
            userSubscriptionService.createBasicUser(newUser.get().getId());

            String name = String.valueOf(request.getName());
            String email = String.valueOf(request.getEmail());
            emailService.sendRegisterMailForJP(name,email);


        }

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public User updateProfile(UpdateProfileRequest updateProfileRequest) {
        User existingUser = userRepository.findById(updateProfileRequest.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update name and email
        existingUser.setName(updateProfileRequest.getName());
        existingUser.setEmail(updateProfileRequest.getEmail());

        // Check if a new password is provided
        if (updateProfileRequest.getNewPassword() != null && !updateProfileRequest.getNewPassword().isEmpty()) {
            // Validate current password
            if (!passwordEncoder.matches(updateProfileRequest.getCurrentPassword(), existingUser.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            // Update to new password
            existingUser.setPassword(passwordEncoder.encode(updateProfileRequest.getNewPassword()));
        }

        // Update role if provided
        existingUser.setRole(updateProfileRequest.getRole());

        return userRepository.save(existingUser);
    }

}
