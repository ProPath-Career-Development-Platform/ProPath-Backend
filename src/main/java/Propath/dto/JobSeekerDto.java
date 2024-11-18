package Propath.dto;

import Propath.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JobSeekerDto {
    private int id;
    private String profilePicture;
    private User user;

}
