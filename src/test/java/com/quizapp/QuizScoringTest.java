package com.quizapp;

import com.quizapp.model.Answer;
import com.quizapp.model.FillInTheBlankQuestion;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.TrueFalseQuestion;
import com.quizapp.model.User;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests quiz scoring.
 */
public class QuizScoringTest {
    @Test
    void quizShouldCalculateScoreCorrectly() {
        Quiz quiz = new Quiz(new User(1, "student"), "test");
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new TrueFalseQuestion("q1", "True", 2.0, 10));
        questions.add(new FillInTheBlankQuestion("q2", "main", 3.0, 10));
        quiz.setQuestions(questions);

        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("True"));
        answers.add(new Answer("wrong"));
        quiz.setAnswers(answers);

        Assertions.assertEquals(2.0, quiz.checkAnswers(), 0.001);
    }
}
