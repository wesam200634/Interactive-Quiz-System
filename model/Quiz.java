package com.quizapp.model;

import java.util.ArrayList;

/**
 * Represents a quiz assigned to a user.
 */
public class Quiz {
    private User user;
    private String title;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    private ArrayList<Question> bank;
    private int numberOfQuestions;

    /**
     * Constructs a quiz.
     *
     * @param user assigned user
     * @param title quiz title
     */
    public Quiz(User user, String title) {
        this.user = user;
        this.title = title;
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.bank = new ArrayList<>();
        this.numberOfQuestions = 0;
    }

    public User getUser() {
        return user;
    }

    public void AddToBank(Question q) {
        if (q != null) {
            bank.add(q);
        }
    }

    public ArrayList<Question> getBank() {
        return new ArrayList<>(bank);
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * UML asks for ArrayList<String> though answers field stores Answer objects.
     *
     * @return answer strings
     */
    public ArrayList<String> getAnswers() {
        ArrayList<String> values = new ArrayList<>();
        for (Answer answer : answers) {
            values.add(answer == null ? "" : answer.getResponse());
        }
        return values;
    }

    public ArrayList<Answer> getAnswerObjects() {
        return answers;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions == null ? new ArrayList<>() : questions;
        this.numberOfQuestions = this.questions.size();
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers == null ? new ArrayList<>() : answers;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    /**
     * Calculates earned points from current answers.
     *
     * @return earned score
     */
    public double checkAnswers() {
        double total = 0.0;
        int bound = Math.min(questions.size(), answers.size());
        for (int i = 0; i < bound; i++) {
            Question question = questions.get(i);
            Answer answer = answers.get(i);
            if (question != null && question.checkAnswer(answer)) {
                total += question.getPoints();
            }
        }
        return total;
    }
}
