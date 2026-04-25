package com.quizapp;

import com.quizapp.model.FillInTheBlankQuestion;
import com.quizapp.model.MultipleChoiceQuestion;
import com.quizapp.model.Question;
import com.quizapp.model.TrueFalseQuestion;
import com.quizapp.service.JsonService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionJsonRoundTripTest {

    @Test
    void saveAndLoadQuestionsShouldRoundTrip() throws Exception {
        JsonService service = new JsonService();
        Path tempDir = Files.createTempDirectory("quizapp-questions-test");
        Path file = tempDir.resolve("questions.json");

        ArrayList<Question> original = new ArrayList<>();
        original.add(new MultipleChoiceQuestion(
                "What does \"JVM\" stand for?\nChoose one.",
                "Java Virtual Machine",
                2.5,
                15,
                List.of("Java Virtual Machine", "Java Variable Method", "Just Very Massive")));
        original.add(new TrueFalseQuestion("Java is platform-independent.", "True", 1.0, 10));
        original.add(new FillInTheBlankQuestion("Entry point method name:", "main", 1.0, 20));

        service.saveQuestions(file, original);
        ArrayList<Question> loaded = service.loadQuestions(file);

        Assertions.assertEquals(3, loaded.size());
        Assertions.assertEquals(original.get(0).getType(), loaded.get(0).getType());
        Assertions.assertEquals(original.get(0).getQuestion_text(), loaded.get(0).getQuestion_text());
        Assertions.assertEquals(original.get(0).getCorrect_ans(), loaded.get(0).getCorrect_ans());
        Assertions.assertEquals(original.get(0).getPoints(), loaded.get(0).getPoints(), 0.0001);
        Assertions.assertEquals(original.get(0).getTimeLimit(), loaded.get(0).getTimeLimit());

        MultipleChoiceQuestion loadedMcq = (MultipleChoiceQuestion) loaded.get(0);
        Assertions.assertTrue(loadedMcq.getChoices().contains("Java Virtual Machine"));

        Assertions.assertEquals("TrueFalse", loaded.get(1).getType());
        Assertions.assertEquals("FillInTheBlank", loaded.get(2).getType());
    }

    @Test
    void questionConstructorShouldSanitizeInvalidValues() {
        FillInTheBlankQuestion q = new FillInTheBlankQuestion(null, null, Double.NaN, -5);
        Assertions.assertNotNull(q.getQuestion_text());
        Assertions.assertNotNull(q.getCorrect_ans());
        Assertions.assertEquals(0.0, q.getPoints(), 0.0001);
        Assertions.assertEquals(0L, q.getTimeLimit());
    }
}

