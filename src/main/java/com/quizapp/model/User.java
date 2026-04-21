package com.quizapp.model;

/**
 * Represents a quiz user.
 */
public class User {
    private int id;
    private String name;
    private double score;
    private String password;
    private boolean banned;
    private String banReason;

    /**
     * Constructs a user.
     *
     * @param id user id
     * @param name user name
     */
    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0.0;
        this.password = "";
        this.banned = false;
        this.banReason = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * UML requires String return.
     *
     * @return score as string
     */
    public String getScore() {
        return String.format("%.2f", score);// Format score to 2 decimal places for clean display
    }

    /**
     * UML requires String parameter.
     *
     * @param score score text
     */
    public void setScore(String score) { // Attempts to convert the input string to a double,if invalid, catches the error and sets score to 0.0
        try {
            this.score = Double.parseDouble(score);
        } catch (NumberFormatException ex) {
            this.score = 0.0;
        }
    }

    public double getScoreValue() {
        return score;
    }

    public void setScoreValue(double score) {
        this.score = score;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;// Assigns the given password; prevents null by replacing it with an empty string
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason == null ? "" : banReason;// same as the password,making the null empty string to avoid errors
    }
}
