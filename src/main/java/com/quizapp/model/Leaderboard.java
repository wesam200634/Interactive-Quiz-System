package com.quizapp.model;

import java.util.ArrayList;// import ArrayList class to store dynamic lists of objects
import java.util.Comparator;// import Comparator interface to define custom sorting logic

/**
 * Leaderboard of users.
 */
public class Leaderboard {
    private ArrayList<User> users; // List that stores all User objects in the leaderboard

    /**
     * Constructs an empty leaderboard.
     */
    public Leaderboard() {
        this.users = new ArrayList<>(); // Constructor initializes the users list to an empty ArrayList
    }

    public void addUser(User u) {
        if (u != null) { //checking if the value isnt null to add it
            users.add(u);
        }
    }

    public void sortByScore() {
        users.sort(Comparator.comparingDouble(User::getScoreValue).reversed()); //using the Comparator to sort the users scores in reverse
    }

    public void displayLeaderboard() {
        sortByScore(); // Sort users by score before displaying the leaderboard
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users == null ? new ArrayList<>() : users; //replacing null with new ArrayList to avoid errors
    }
}
