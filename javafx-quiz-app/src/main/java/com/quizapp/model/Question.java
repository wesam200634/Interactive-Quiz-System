package com.quizapp.model;

import com.quizapp.service.ShuffleService;
import java.util.ArrayList;
import java.util.List;

/**
 * Base abstract question type.
 */
public abstract class Question {
    
    private String question_text;
   
    private String correct_ans;
    
    private double points;
    
    private long timeLimit;

    /**
     * Constructs a question.
     *
     * @param question_text question text
     * @param correct_ans correct answer
     * @param points points value
     * @param timeLimit time limit in seconds
     */
    public Question(String question_text, String correct_ans, double points, long timeLimit) {
    
        this.question_text = question_text;
        
        this.correct_ans = correct_ans;
        
        this.points = points;
        
        this.timeLimit = timeLimit;
    
    }

    /**
     * Returns the question type name.
     *
     * @return type name
     */
    public abstract String getType();

    /**
     * Manual shuffle hook required by UML.
     * Concrete question types without choices do nothing.
     */
    public void shuffleQuestions() {
        // Default no-op for non-choice-based questions.
    }

    /**
     * Checks whether the provided answer is correct.
     *
     * @param a answer to check
     * @return true when correct
     */
    public abstract boolean checkAnswer(Answer a);

    /**
     * Normalizes text for robust comparisons.
     *
     * @param text value to normalize
     * @return normalized string
     */
    protected String normalize(String text) {
   
        return text == null ? "" : text.trim().replaceAll("\\s+", " ").toLowerCase();
    
    }

    /**
     * Shuffles a list of strings using Fisher-Yates.
     *
     * @param values values to shuffle
     */
    protected void shuffleList(List<String> values) {
    
        if (values == null || values.size() <= 1) return;

    for (int i = values.size() - 1; i > 0; i--) {

        int j = (int) (Math.random() * (i + 1));

        // Swap elements
        String temp = values.get(i);

        values.set(i, values.get(j));

        values.set(j, temp);

    }
    }

    public String getQuestion_text() {

        return question_text;

    }

    public void setQuestion_text(String question_text) {

        this.question_text = question_text;

    }

    public String getCorrect_ans() {

        return correct_ans;

    }

    
    public void setCorrect_ans(String correct_ans) {
    
        this.correct_ans = correct_ans;
    
    }

    public double getPoints() {
    
        return points;
    
    }

    public void setPoints(double points) {
    
        this.points = points;
    
    }

    public long getTimeLimit() {
    
        return timeLimit;
    
    }

    public void setTimeLimit(long timeLimit) {
    
        this.timeLimit = timeLimit;
    
    }
}
