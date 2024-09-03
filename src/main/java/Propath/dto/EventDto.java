package Propath.dto;

import Propath.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private Long id;
    private String title;
    private String date;
    private String banner;
    private String startTime;
    private String endTime;
    private int maxParticipant;
    private String closeDate;
    private String location;
    private double latitude;
    private double longitude;
    private List<String> keyWords;
    private String description;
    private Boolean delete;
    private String status;
    private User user;


}
