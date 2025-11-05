package edu.uga.cs.statecapitalsquiz.models;

/**
 * Represents a single quiz question with the state and three randomized city choices.
 * One city is the correct capital, the other two are large cities in that state.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class QuizQuestion {
    private StateItem state;
    private String[] answerChoices;  // 3 cities in random order
    private int correctAnswerIndex;  // Index (0-2) of correct answer in choices array

    /**
     * Constructor with randomized answer choices
     *
     * @param state The state this question is about
     * @param randomizedChoices Array of 3 cities in random order
     */
    public QuizQuestion(StateItem state, String[] randomizedChoices) {
        this.state = state;
        this.answerChoices = randomizedChoices;

        // Find index of correct answer (capital city)
        for (int i = 0; i < randomizedChoices.length; i++) {
            if (randomizedChoices[i].equals(state.getCapitalCity())) {
                correctAnswerIndex = i;
                break;
            }
        }
    }

    /**
     * Check if the given answer is correct
     *
     * @param answer The city name chosen by the user
     * @return true if answer matches the capital city
     */
    public boolean isCorrect(String answer) {
        return answer != null && answer.equals(state.getCapitalCity());
    }

    /**
     * Get the question text
     *
     * @return Question string like "What is the capital of Georgia?"
     */
    public String getQuestionText() {
        return "What is the capital of " + state.getStateName() + "?";
    }

    // Getters
    public StateItem getState() {
        return state;
    }

    public String getStateName() {
        return state.getStateName();
    }

    public String[] getAnswerChoices() {
        return answerChoices;
    }

    public String getCorrectAnswer() {
        return state.getCapitalCity();
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "state=" + state.getStateName() +
                ", correctAnswer=" + state.getCapitalCity() +
                '}';
    }
}