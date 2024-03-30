package com.DheerajKumar.QuizApp.dao;

import com.DheerajKumar.QuizApp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {
    List<Question> findAllByCategory(String category);
}
