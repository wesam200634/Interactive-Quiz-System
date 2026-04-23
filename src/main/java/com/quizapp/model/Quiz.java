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
        this.questions = new ArrayList<>();//making new questions Arraylist
        this.answers = new ArrayList<>();//making new answered Arraylist
        this.bank = new ArrayList<>();//making new questionsbank Arraylist
        this.numberOfQuestions = 0;
    }

    public User getUser() {
        return user; // Returns the user associated with this quiz
    }

    public void AddToBank(Question q) {
        if (q != null) {
            bank.add(q); //adding the question to the questionbank
        }
    }

    public ArrayList<Question> getBank() {
        return new ArrayList<>(bank); // Returns a copy of the bank to protect internal data from modification
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
    public ArrayList<String> getAnswers() { // returns a list of answer responses as strings, replacing null answers with empty strings
        ArrayList<String> values = new ArrayList<>();
        for (Answer answer : answers) {
            values.add(answer == null ? "" : answer.getResponse()); 
        }
        return values;
    }

    public ArrayList<Answer> getAnswerObjects() {
        return answers;
    }

    public void setQuestions(ArrayList<Question> questions) { // Sets questions safely (avoids null) and updates the number of questions
        this.questions = questions == null ? new ArrayList<>() : questions;
        this.numberOfQuestions = this.questions.size();
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers == null ? new ArrayList<>() : answers;// Sets answers safely (avoids null)
    }

    public int getNumberOfQuestions() { // Returns the number of questions
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) { // Sets the number of questions
        this.numberOfQuestions = numberOfQuestions;
    }

    /**
     * Calculates earned points from current answers.
     *
     * @return earned score
     */
    public double checkAnswers() {     // Checks all answers, calculates total score based on correct responses
        double total = 0.0;
        int bound = Math.min(questions.size(), answers.size());     //taking the minmum for protection,in case some question or answer deleted
        for (int i = 0; i < bound; i++) {    // walk throught every question
            Question question = questions.get(i);    // taking i question
            Answer answer = answers.get(i);    //taking i answer
            if (question != null && question.checkAnswer(answer)) {     // checking if the answer of this question is true,as long as its not null
                total += question.getPoints();    //if its true,increasing the points
            }
        }
        return total;
    }
}
