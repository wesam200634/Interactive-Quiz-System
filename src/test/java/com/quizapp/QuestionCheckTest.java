package com.quizapp;

import com.quizapp.model.Answer;
import com.quizapp.model.FillInTheBlankQuestion;
import com.quizapp.model.MultipleChoiceQuestion;
import com.quizapp.model.TrueFalseQuestion;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests question answer checking.
 */
public class QuestionCheckTest {
    @Test
    void multipleChoiceShouldCheckCorrectly() {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion("q", "extends", 1.0, 20, List.of("extends", "implements"));
        Assertions.assertTrue(question.checkAnswer(new Answer("extends")));
        Assertions.assertFalse(question.checkAnswer(new Answer("implements")));
    }

    @Test
    void trueFalseShouldCheckCorrectly() {
        TrueFalseQuestion question = new TrueFalseQuestion("q", "True", 1.0, 10);
        Assertions.assertTrue(question.checkAnswer(new Answer("true")));
        Assertions.assertFalse(question.checkAnswer(new Answer("false")));
    }

    @Test
    void fillBlankShouldIgnoreCaseAndSpaces() {
        FillInTheBlankQuestion question = new FillInTheBlankQuestion("q", "main", 1.0, 20);
        Assertions.assertTrue(question.checkAnswer(new Answer("  MAIN   ")));
    }
}
