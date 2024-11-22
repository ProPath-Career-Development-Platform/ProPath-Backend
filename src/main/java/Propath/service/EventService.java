package Propath.service;


import Propath.dto.EventDto;
import Propath.dto.JobSeekerEventDto;
import Propath.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {

  EventDto saveEvent (EventDto eventDto);
  EventDto updateEvent (Long eventID, EventDto updateEventDto);
  void removeEvent(Long eventID);
  EventDto getEventById(Long eventID);
  List<EventDto> getAllEvent();


    List<JobSeekerEventDto> getRegisteredusers(Long id);

  List<EventDto> getAllPostedEvent();
}
