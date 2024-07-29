package Propath.mapper;

import Propath.dto.CustomQuestionDto;
import Propath.model.CustomQuestions;

public class CustomQuestionMapper {

    public static CustomQuestions toEntity(CustomQuestionDto customQuestionDto) {
        if (customQuestionDto == null) {
            return null;
        }

        CustomQuestions customQuestions = new CustomQuestions();
        customQuestions.setId(customQuestionDto.getId());
        customQuestions.setQuestionText(customQuestionDto.getQuestionText());
        System.out.println("Mapped to Entity: " + customQuestions.getQuestionText()); // Debugging line
        return customQuestions;
    }

    public static CustomQuestionDto toDto(CustomQuestions customQuestions) {
        if (customQuestions == null) {
            return null;
        }

        CustomQuestionDto customQuestionDto = new CustomQuestionDto();
        customQuestionDto.setId(customQuestions.getId());
        customQuestionDto.setQuestionText(customQuestions.getQuestionText());
        System.out.println("Mapped to DTO: " + customQuestionDto.getQuestionText()); // Debugging line
        return customQuestionDto;
    }
}
