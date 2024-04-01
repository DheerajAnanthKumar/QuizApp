package com.DheerajKumar.QuizApp.service;

import com.DheerajKumar.QuizApp.dao.QuestionDao;
import com.DheerajKumar.QuizApp.dao.QuizDao;
import com.DheerajKumar.QuizApp.model.Question;
import com.DheerajKumar.QuizApp.model.QuestionWrapper;
import com.DheerajKumar.QuizApp.model.Quiz;
import com.DheerajKumar.QuizApp.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;

    public QuizService(QuizDao quizDao, QuestionDao questionDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
    }

    public ResponseEntity<String> createQuiz(
            String category, int numQ, String title
    ) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category,numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(
            int id
    ) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionFromDb = quiz.get().getQuestions();
        List<QuestionWrapper> questionForUsers = new ArrayList<>();

        for(Question q: questionFromDb){
            QuestionWrapper qw = new QuestionWrapper(
                    q.getId(), q.getQuestion_title(),
                    q.getOption1(),q.getOption2(),
                    q.getOption3(),q.getOption4()
            );
            questionForUsers.add(qw);
        }

        return new ResponseEntity<>(questionForUsers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(
            int id, List<Response> responses
    ) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questions = quiz.get().getQuestions();
        int count = 0;
        int i = 0;
        for(Response response: responses){
            if(response.getResponse().equals(questions.get(i).getRight_answer())){
                count++;
            }
            i++;
        }
        return new ResponseEntity<>(count,HttpStatus.OK);
    }
}
