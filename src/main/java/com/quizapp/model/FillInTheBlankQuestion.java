package com.quizapp.model;

/**
 * Fill-in-the-blank question implementation.
 */
public class FillInTheBlankQuestion extends Question {

    /**
     * Constructs a fill-in-the-blank question.
     *
     * @param question_text question text
     * @param correct_ans correct answer
     * @param points points value
     * @param timeLimit time limit in seconds
     */
    public FillInTheBlankQuestion(String question_text, String correct_ans, double points, long timeLimit) {
        super(question_text, correct_ans, points, timeLimit);
    }

    @Override
    public String getType() {
        return "FillInTheBlank";
    }

    @Override
    public boolean checkAnswer(Answer a) {
        return normalize(a == null ? "" : a.getResponse()).equals(normalize(getCorrect_ans()));
    }
}
