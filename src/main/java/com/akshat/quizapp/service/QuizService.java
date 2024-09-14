package com.akshat.quizapp.service;

import com.akshat.quizapp.dao.QuestionDao;
import com.akshat.quizapp.dao.QuizDao;
import com.akshat.quizapp.model.Question;
import com.akshat.quizapp.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category,numQ);
        Quiz quiz = new Quiz();
        quiz.setQuestions(questions);
        quiz.setTitle(title);
        quizDao.save(quiz);

        return new ResponseEntity<>("success", HttpStatus.CREATED);


    }
}
