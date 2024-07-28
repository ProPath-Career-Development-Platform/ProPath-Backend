package Propath.dto;

import Propath.model.CustomQuestions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomQuestionDto {
    private Long id;
    private String questionText;
}

