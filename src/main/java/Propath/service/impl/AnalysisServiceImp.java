package Propath.service.impl;

import Propath.model.Applicant;
import Propath.model.Event;
import Propath.model.Job;
import Propath.model.User;
import Propath.repository.ApplicantRepository;
import Propath.repository.EventRepository;
import Propath.repository.JobRepository;
import Propath.repository.UserRepository;
import Propath.service.AnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@AllArgsConstructor
public class AnalysisServiceImp implements AnalysisService {

    private ApplicantRepository applicantRepository;
    private JobRepository jobRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getHomeAnalysis(){

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Job> jobs = jobRepository.findByUserAndStatus(user,"active");

        List<String> statuses = Arrays.asList("preSelected", "selected","pending");
        List<Applicant> applicants = applicantRepository.findByJobUserAndStatusIn(user,statuses);

        List<Event> events = eventRepository.findByUserAndDeleteFalseAndStatus(user,"active");

        Map<String,Object> analisis = new HashMap<>();

        analisis.put("job",jobs.size());
        analisis.put("applicants", applicants.size());
        analisis.put("events", events.size());

        return analisis;



    }


}
