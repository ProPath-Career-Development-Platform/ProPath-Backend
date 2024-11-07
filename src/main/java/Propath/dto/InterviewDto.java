package Propath.dto;

import Propath.model.Job;
import Propath.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDto {

    private Long id;
    private Long duration;
    private List<LocalTime> timeSlot;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    private Date interviewDate;
    private User user;
    private Job job;


}
