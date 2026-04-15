package com.quizapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple choice question implementation.
 */
public class MultipleChoiceQuestion extends Question {
    private List<String> choices;

    /**
     * Constructs a multiple choice question.
     *
     * @param question_text question text
     * @param correct_ans correct answer
     * @param points points value
     * @param timeLimit time limit in seconds
     * @param choices answer choices
     */
    public MultipleChoiceQuestion(String question_text, String correct_ans, double points, long timeLimit, List<String> choices) {
        super(question_text, correct_ans, points, timeLimit);
        this.choices = choices == null ? new ArrayList<>() : new ArrayList<>(choices);
    }

    @Override
    public String getType() {
        return "MultipleChoice";
    }

    @Override
    public void shuffleQuestions() {
        shuffleList(choices);
    }

    /**
     * Placeholder method required by UML.
     */
    public void displayChoices() {
        // GUI renders choices directly.
    }

    @Override
    public boolean checkAnswer(Answer a) {
        return normalize(a == null ? "" : a.getResponse()).equals(normalize(getCorrect_ans()));
    }

    public List<String> getChoices() {
        return new ArrayList<>(choices);
    }

    public void setChoices(List<String> choices) {
        this.choices = choices == null ? new ArrayList<>() : new ArrayList<>(choices);
    }
}
