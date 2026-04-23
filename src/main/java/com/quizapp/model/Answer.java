package com.quizapp.model;

/**
 * Represents a user's answer to a question.
 */
public class Answer {
    private String userResponse;

    /**
     * Creates an empty answer.
     */
    public Answer() {
        this.userResponse = "";    //initialize the respone
    }

    /**
     * Creates an answer with a response.
     *
     * @param userResponse response text
     */
    public Answer(String userResponse) {
        this.userResponse = userResponse == null ? "" : userResponse;    //taking the user respone and store it,if null its ""
    }

    /**
     * Returns the current response.
     *
     * @return response value
     */
    public String getResponse() {
        return userResponse;     // returns the user's response for checking later
    }

    /**
     * Sets the current response.
     *
     * @param response response value
     */
    public void setResponse(String response) {
        this.userResponse = response == null ? "" : response;   // Sets the user's response, replacing null with an empty string to avoid errors
    }
}
