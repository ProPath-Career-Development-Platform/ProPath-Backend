package Propath.service.impl;

import Propath.dto.EventDto;
import Propath.mapper.EventMapper;
import Propath.model.Event;
import Propath.model.User;
import Propath.repository.EventRepository;
import Propath.repository.UserRepository;
import Propath.service.EventService;
import org.springframework.data.domain.Page;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImp  implements EventService {

  private  EventRepository eventRepository;
  private UserRepository userRepository;



    @Override
    public EventDto saveEvent(EventDto eventDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();  // Get the username of the logged-in user

        // Assuming you have a method to find the user by username
        Optional<User> userOptional = userRepository.findByEmail(userEmail); // Implement this method in your UserRepository

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found"); // Handle the case when the user is not found
        }

        User user = userOptional.get();

        Event event = EventMapper.maptoEvent(eventDto);
        event.setUser(user); // Set the user for the event
        System.out.println("Event Details: " + event);
        Event savedEvent = eventRepository.save(event);

        return EventMapper.maptoEventDto(savedEvent);
    }

    @Override
    public EventDto updateEvent(Long eventId, EventDto updateEventDto) {
        // Fetch the existing event from the repository
        Optional<Event> existingEventOptional = eventRepository.findById(eventId);

        if (existingEventOptional.isEmpty()) {
            throw new RuntimeException("Event not found"); // Handle the case when the event is not found
        }

        Event existingEvent = existingEventOptional.get();

        // Update the existing event with new values
        existingEvent.setTitle(updateEventDto.getTitle());
        existingEvent.setDate(updateEventDto.getDate());
        existingEvent.setBanner(updateEventDto.getBanner());
        existingEvent.setStartTime(updateEventDto.getStartTime());
        existingEvent.setEndTime(updateEventDto.getEndTime());
        existingEvent.setMaxParticipant(updateEventDto.getMaxParticipant());
        existingEvent.setCloseDate(updateEventDto.getCloseDate());
        existingEvent.setLocation(updateEventDto.getLocation());
        existingEvent.setKeyWords(updateEventDto.getKeyWords());
        existingEvent.setDescription(updateEventDto.getDescription());
        existingEvent.setLatitude(updateEventDto.getLatitude());
        existingEvent.setLongitude(updateEventDto.getLongitude());

        // Save the updated event back to the repository
        Event updatedEvent = eventRepository.save(existingEvent);

        return EventMapper.maptoEventDto(updatedEvent);
    }

    @Override
    public void removeEvent(Long eventID){
        // Fetch the existing event from the repository
        Optional<Event> existingEventOptional = eventRepository.findById(eventID);

        if (existingEventOptional.isEmpty()) {
            throw new RuntimeException("Event not found"); // Handle the case when the event is not found
        }

        Event existingEvent = existingEventOptional.get();

        existingEvent.setDelete(true);

        eventRepository.save(existingEvent);



    }

    @Override
    public List<EventDto> getAllEvent(){

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch events associated with the user's ID
        List<Event> events = eventRepository.findByUserIdAndDeleteFalse(user.getId());

        // Convert Event entities to EventDto
        return events.stream()
                .map(EventMapper::maptoEventDto)
                .collect(Collectors.toList());

    }

    @Override
    public EventDto getEventById(Long eventID) {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming you store the email in the principal

        // Find the user by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the event associated with the eventID
        Event event = eventRepository.findByIdAndDeleteFalse(eventID)
                .orElseThrow(() -> new RuntimeException("Event not found"));


        // Check if the event belongs to the currently authenticated user
        if (event.getUser().getId() != (user.getId())) {
            throw new RuntimeException("Unauthorized request: user does not own this event.");
        }

        // Convert Event entity to EventDto
        return EventMapper.maptoEventDto(event);
    }






}
