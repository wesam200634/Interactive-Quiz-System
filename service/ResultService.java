package com.quizapp.service;

import com.quizapp.model.Answer;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.User;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes quiz results to the results folder.
 */
public class ResultService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private final JsonService jsonService;

    /**
     * Constructs the result service.
     */
    public ResultService() {
        this.jsonService = new JsonService();
    }

    /**
     * Saves a quiz result report.
     *
     * @param resultsDir results directory
     * @param quiz quiz instance
     * @param timeTakenSeconds elapsed time in seconds
     * @return saved file path
     * @throws IOException on I/O failure
     */
    public Path saveResult(Path resultsDir, Quiz quiz, long timeTakenSeconds) throws IOException {
        StringBuilder builder = new StringBuilder();
        User user = quiz.getUser();
        builder.append("User: ").append(user.getName()).append(System.lineSeparator());
        builder.append("Quiz: ").append(quiz.getTitle()).append(System.lineSeparator());
        builder.append("Score: ").append(quiz.checkAnswers()).append(System.lineSeparator());
        builder.append("Time Taken (seconds): ").append(timeTakenSeconds).append(System.lineSeparator());
        builder.append(System.lineSeparator()).append("Review:").append(System.lineSeparator());

        int bound = Math.min(quiz.getQuestions().size(), quiz.getAnswerObjects().size());
        for (int i = 0; i < bound; i++) {
            Question question = quiz.getQuestions().get(i);
            Answer answer = quiz.getAnswerObjects().get(i);
            builder.append(i + 1).append(". ").append(question.getQuestion_text()).append(System.lineSeparator());
            builder.append("   User Answer: ").append(answer.getResponse()).append(System.lineSeparator());
            builder.append("   Correct Answer: ").append(question.getCorrect_ans()).append(System.lineSeparator());
            builder.append("   Result: ").append(question.checkAnswer(answer) ? "Correct" : "Wrong").append(System.lineSeparator());
        }

        String fileName = "result_" + FORMATTER.format(LocalDateTime.now()) + ".txt";
        Path file = resultsDir.resolve(fileName);
        jsonService.writeText(file, builder.toString());
        return file;
    }
}
