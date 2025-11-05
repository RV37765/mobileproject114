package edu.uga.cs.statecapitalsquiz.models;

/**
 * Represents a quiz instance containing 6 randomly selected states.
 * Tracks the quiz score, date/time, and progress (questions answered).
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class Quiz {
    private int id;
    private String date;
    private int score;
    private int questionsAnswered;
    private int[] stateIds;  // Array of 6 state IDs

    /**
     * Default constructor
     */
    public Quiz() {
        this.stateIds = new int[6];
        this.score = 0;
        this.questionsAnswered = 0;
    }

    /**
     * Constructor with state IDs
     */
    public Quiz(int[] stateIds) {
        this.stateIds = stateIds;
        this.score = 0;
        this.questionsAnswered = 0;
    }

    /**
     * Check if quiz is complete (all 6 questions answered)
     */
    public boolean isComplete() {
        return questionsAnswered >= 6;
    }

    /**
     * Increment the score by 1
     */
    public void incrementScore() {
        this.score++;
    }

    /**
     * Increment questions answered by 1
     */
    public void incrementQuestionsAnswered() {
        this.questionsAnswered++;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public int[] getStateIds() {
        return stateIds;
    }

    public void setStateIds(int[] stateIds) {
        this.stateIds = stateIds;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", score=" + score +
                ", questionsAnswered=" + questionsAnswered +
                '}';
    }
}