package com.quizapp.service;

import com.quizapp.model.Question;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Loads quiz questions.
 */
public class QuizDataService {
    private final JsonService jsonService;
    private final Path questionsFile;

    /**
     * Constructs the quiz data service.
     *
     * @param questionsFile runtime questions path
     */
    public QuizDataService(Path questionsFile) {
        this.jsonService = new JsonService();
        this.questionsFile = questionsFile;
    }

    /**
     * Initializes the runtime questions file if needed.
     *
     * @throws IOException on I/O failure
     */
    public void initialize() throws IOException {
        jsonService.ensureQuestionsFileExists("/questions.json", questionsFile);
    }

    /**
     * Loads all questions.
     *
     * @return question list
     * @throws IOException on I/O failure
     */
    public ArrayList<Question> loadQuestions() throws IOException {
        return jsonService.loadQuestions(questionsFile);
    }

    /**
     * Saves all questions back to the runtime file.
     *
     * @param questions question list
     * @throws IOException on I/O failure
     */
    public void saveQuestions(ArrayList<Question> questions) throws IOException {
        jsonService.saveQuestions(questionsFile, questions);
    }

    public Path getQuestionsFile() {
        return questionsFile;
    }
}
