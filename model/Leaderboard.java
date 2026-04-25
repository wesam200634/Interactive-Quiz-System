package com.quizapp.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Leaderboard of users.
 */
public class Leaderboard {
    private ArrayList<User> users;

    /**
     * Constructs an empty leaderboard.
     */
    public Leaderboard() {
        this.users = new ArrayList<>();
    }

    public void addUser(User u) {
        if (u != null) {
            users.add(u);
        }
    }

    public void sortByScore() {
        users.sort(Comparator.comparingDouble(User::getScoreValue).reversed());
    }

    public void displayLeaderboard() {
        sortByScore();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users == null ? new ArrayList<>() : users;
    }
}
