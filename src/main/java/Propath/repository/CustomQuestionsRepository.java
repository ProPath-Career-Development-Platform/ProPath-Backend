package Propath.repository;

import Propath.model.CustomQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomQuestionsRepository extends JpaRepository<CustomQuestions, Integer> {
}
