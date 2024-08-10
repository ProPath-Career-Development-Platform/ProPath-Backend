package Propath.mapper;

import Propath.dto.EventDto;
import Propath.model.Event;
import Propath.repository.EventRepository;

public class EventMapper {

    private final EventRepository eventRepository;

    public EventMapper(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public static EventDto maptoEventDto (Event event){
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDate(),
                event.getBanner(),
                event.getStartTime(),
                event.getEndTime(),
                event.getMaxParticipant(),
                event.getCloseDate(),
                event.getLocation(),
                event.getLatitude(),
                event.getLongitude(),
                event.getKeyWords(),
                event.getDescription(),
                event.getDelete(),
                event.getStatus(),
                event.getUser()



        );
    }

    public static Event maptoEvent (EventDto eventDto){
        return new Event(
                eventDto.getId(),
                eventDto.getTitle(),
                eventDto.getDate(),
                eventDto.getBanner(),
                eventDto.getStartTime(),
                eventDto.getEndTime(),
                eventDto.getMaxParticipant(),
                eventDto.getCloseDate(),
                eventDto.getLocation(),
                eventDto.getLatitude(),
                eventDto.getLongitude(),
                eventDto.getKeyWords(),
                eventDto.getDescription(),
                eventDto.getDelete(),
                eventDto.getStatus(),
                eventDto.getUser()

        );
    }


}
