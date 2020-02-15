package io.hanyoung.gulmatebackend.web.question;

import io.hanyoung.gulmatebackend.config.web.QuestionCategoryConverter;
import io.hanyoung.gulmatebackend.web.question.dto.Question;
import io.hanyoung.gulmatebackend.web.question.dto.QuestionCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {

    @PostMapping("/question")
    public ResponseEntity<?> get(@RequestBody Question question) {
        return ResponseEntity.ok(question);
    }

    private List getQuestionsByCategory(QuestionCategory category) {
        List questions = new ArrayList();
        Question question = new Question();
        question.setType(category);
        if(category == QuestionCategory.CSE){
            question.setQuestion("What is Operating System.");
            question.setAnswer("This is the answer of what is os.");
        } else if(category == QuestionCategory.ECE){
            question.setQuestion("What is a transistor.");
            question.setAnswer("This is the answer of what is transistor.");
        }
        questions.add(question);
        return questions;
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(QuestionCategory.class, new QuestionCategoryEnumTypeEditor());
    }

}
