package com.quizapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores reusable questions.
 */
public class QuestionBank {
    private final ArrayList<Question> questions;

    /**
     * Creates an empty question bank.
     */
    public QuestionBank() {
        this.questions = new ArrayList<>();
    }

    /**
     * Adds a question to the bank.
     *
     * @param question question to add
     */
    public void addQuestion(Question question) {
        if (question != null) {
            questions.add(question);
        }
    }

    /**
     * Returns the bank content.
     *
     * @return list copy
     */
    public ArrayList<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    /**
     * Replaces the bank content.
     *
     * @param newQuestions questions to store
     */
    public void setQuestions(List<Question> newQuestions) {
        questions.clear();
        if (newQuestions != null) {
            questions.addAll(newQuestions);
        }
    }
}
