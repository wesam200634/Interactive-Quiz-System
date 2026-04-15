package com.quizapp.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Administrative user.
 */
public class Admin extends User {
    private EnumSet<AdminPermission> permissions;
    private QuestionBank questionBank;

    /**
     * Constructs an admin.
     *
     * @param id admin id
     * @param name admin name
     */
    public Admin(int id, String name) {
        super(id, name);
        this.permissions = EnumSet.allOf(AdminPermission.class);
        this.questionBank = new QuestionBank();
    }

    public Quiz createQuiz(String title, User assignedTo, List<Question> questions) {
        Quiz quiz = new Quiz(assignedTo, title);
        ArrayList<Question> list = new ArrayList<>(questions == null ? List.of() : questions);
        quiz.setQuestions(list);
        ArrayList<Answer> answers = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            answers.add(new Answer());
        }
        quiz.setAnswers(answers);
        return quiz;
    }

    public void addQuestionToBank(Question q) {
        questionBank.addQuestion(q);
    }

    public void publishQuiz(Quiz quiz) {
        // Publishing is handled by the persistence service.
    }

    public User createUser(int id, String name, char[] rawPassword) {
        User user = new User(id, name);
        user.setPassword(new String(rawPassword));
        return user;
    }

    public void banUser(User user, String reason) {
        if (user != null) {
            user.setBanned(true);
            user.setBanReason(reason);
        }
    }

    public void resetScore(User user) {
        if (user != null) {
            user.setScoreValue(0.0);
        }
    }

    public EnumSet<AdminPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(EnumSet<AdminPermission> permissions) {
        this.permissions = permissions;
    }

    public QuestionBank getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }
}
