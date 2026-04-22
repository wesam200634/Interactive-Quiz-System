package com.quizapp.model;

import java.util.ArrayList;
import java.util.EnumSet; // import EnumSet for efficient sets of enum constants
import java.util.List; // import it to use a general list type (allows flexibility in implementation)

/**
 * Administrative user.
 */
public class Admin extends User { //Admid class inherited user class
    private EnumSet<AdminPermission> permissions; // Stores a set of admin permissions (enum values) efficiently
    private QuestionBank questionBank; //object questionBand from QuestionBank classk

    /**
     * Constructs an admin.
     *
     * @param id admin id
     * @param name admin name
     */
    public Admin(int id, String name) {
        super(id, name);// calling the father class constructer(User class)
        this.permissions = EnumSet.allOf(AdminPermission.class);//giving the admin all premissions
        this.questionBank = new QuestionBank();
    }

    public Quiz createQuiz(String title, User assignedTo, List<Question> questions) { // Creates a new Quiz assigned to a user with a given title
        Quiz quiz = new Quiz(assignedTo, title);
        ArrayList<Question> list = new ArrayList<>(questions == null ? List.of() : questions); // Convert input questions to ArrayList; if null, use an empty list to avoid errors
        quiz.setQuestions(list);   // Assign the questions to the quiz
        ArrayList<Answer> answers = new ArrayList<>();  // Create an empty list to store answers
        for (int i = 0; i < list.size(); i++) {  // Initialize an empty Answer for each question
            answers.add(new Answer());
        }
        quiz.setAnswers(answers);
        return quiz;
    }

    public void addQuestionToBank(Question q) { 
        questionBank.addQuestion(q); // Adds a question to the question bank
    }

    public void publishQuiz(Quiz quiz) {// later expansion
        // Publishing is handled by the persistence service.
    }

    public User createUser(int id, String name, char[] rawPassword) {
        User user = new User(id, name); // Creates a new User, converts the password from char[] to String, and assigns it
        user.setPassword(new String(rawPassword));
        return user;
    }

    public void banUser(User user, String reason) {
        if (user != null) { // Bans the given user and sets the reason, if the user is not null
            user.setBanned(true);
            user.setBanReason(reason);
        }
    }

    public void resetScore(User user) {
        if (user != null) {
            user.setScoreValue(0.0);// reset the user's score if the user is not null.
        }
    }

    public EnumSet<AdminPermission> getPermissions() {
        return permissions; // returns the set of admin permissions
    }

    public void setPermissions(EnumSet<AdminPermission> permissions) {
        this.permissions = permissions; // Sets the admin's permissions
    }

    public QuestionBank getQuestionBank() {
        return questionBank; // returns the QuestionBank object associated with the admin
    }

    public void setQuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank; // Sets the QuestionBank for this admin
    }
}
