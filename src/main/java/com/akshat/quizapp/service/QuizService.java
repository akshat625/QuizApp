package com.akshat.quizapp.service;

import com.akshat.quizapp.dao.QuestionDao;
import com.akshat.quizapp.dao.QuizDao;
import com.akshat.quizapp.model.Question;
import com.akshat.quizapp.model.QuestionWrapper;
import com.akshat.quizapp.model.Quiz;
import com.akshat.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try{
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category,numQ);
            Quiz quiz = new Quiz();
            quiz.setQuestions(questions);
            quiz.setTitle(title);
            quizDao.save(quiz);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        try{
            Optional<Quiz> quiz = quizDao.findById(id);
            List<Question> questionsFromDB = quiz.get().getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();
            for(Question q : questionsFromDB){
                QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(),q.getQuestion(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
                questionsForUser.add(questionWrapper);
            }
            return new ResponseEntity<>(questionsForUser,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);


    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        try{
            Optional<Quiz> quiz = quizDao.findById(id);
            List<Question> questions = quiz.get().getQuestions();
            int score = 0;
            for(int i=0;i<questions.size();i++){
                if(questions.get(i).getRight_answer().equals(responses.get(i).getMarkedAnswer()))
                    score++;
            }
            return new ResponseEntity<>(score,HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(-1,HttpStatus.BAD_REQUEST);
    }
}
