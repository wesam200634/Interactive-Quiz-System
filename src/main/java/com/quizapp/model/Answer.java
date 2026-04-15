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
        this.userResponse = "";
    }

    /**
     * Creates an answer with a response.
     *
     * @param userResponse response text
     */
    public Answer(String userResponse) {
        this.userResponse = userResponse == null ? "" : userResponse;
    }

    /**
     * Returns the current response.
     *
     * @return response value
     */
    public String getResponse() {
        return userResponse;
    }

    /**
     * Sets the current response.
     *
     * @param response response value
     */
    public void setResponse(String response) {
        this.userResponse = response == null ? "" : response;
    }
}
