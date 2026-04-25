package com.quizapp.model;

/**
 * True/False question implementation.
 */
public class TrueFalseQuestion extends Question {

    /**
     * Constructs a true/false question.
     *
     * @param question_text question text
     * @param correct_ans correct answer
     * @param points points value
     * @param timeLimit time limit in seconds
     */
    public TrueFalseQuestion(String question_text, String correct_ans, double points, long timeLimit) {
        super(question_text, correct_ans, points, timeLimit);
    }

    @Override
    public String getType() {
        return "TrueFalse";
    }

    /**
     * Placeholder method required by UML.
     */
    public void displayChoices() {
        // GUI renders True/False directly.
    }

    @Override
    public boolean checkAnswer(Answer a) {
        return normalize(a == null ? "" : a.getResponse()).equals(normalize(getCorrect_ans()));
    }
}
