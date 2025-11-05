package edu.uga.cs.statecapitalsquiz.utils;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.models.QuizQuestion;
import edu.uga.cs.statecapitalsquiz.models.StateItem;

/**
 * QuizManager handles quiz creation and question generation logic.
 * Works with the Quiz and QuizQuestion models to create randomized quizzes.
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Generate 6 random unique questions from state data</li>
 *   <li>Randomize answer choices for each question</li>
 *   <li>Track quiz state and scoring</li>
 * </ul>
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class QuizManager {

    private static final String TAG = "QuizManager";
    private static final int QUESTIONS_PER_QUIZ = 6;

    private List<StateItem> allStates;
    private Quiz currentQuiz;
    private List<QuizQuestion> currentQuestions;
    private Random random;
    private SimpleDateFormat dateFormat;

    /**
     * Constructor
     */
    public QuizManager() {
        this.random = new Random();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    }

    /**
     * Set available states for quiz generation
     *
     * @param states List of all states from database
     */
    public void setAllStates(List<StateItem> states) {
        this.allStates = states;
        Log.d(TAG, "Loaded " + states.size() + " states");
    }

    /**
     * Create a new quiz with 6 random unique questions
     *
     * @return New Quiz object
     * @throws IllegalStateException if not enough states available
     */
    public Quiz createNewQuiz() {
        if (allStates == null || allStates.size() < QUESTIONS_PER_QUIZ) {
            Log.e(TAG, "Not enough states to create quiz");
            throw new IllegalStateException("Need at least " + QUESTIONS_PER_QUIZ + " states");
        }

        // Select 6 random unique states
        List<StateItem> shuffled = new ArrayList<>(allStates);
        Collections.shuffle(shuffled, random);
        List<StateItem> selectedStates = shuffled.subList(0, QUESTIONS_PER_QUIZ);

        // Create quiz with state IDs
        int[] stateIds = new int[QUESTIONS_PER_QUIZ];
        for (int i = 0; i < selectedStates.size(); i++) {
            stateIds[i] = selectedStates.get(i).getId();
        }

        currentQuiz = new Quiz(stateIds);
        currentQuiz.setDate(dateFormat.format(new Date()));

        // Generate questions
        currentQuestions = new ArrayList<>();
        for (StateItem state : selectedStates) {
            QuizQuestion question = createQuestionForState(state);
            currentQuestions.add(question);
        }

        Log.d(TAG, "Created new quiz with " + currentQuestions.size() + " questions");
        return currentQuiz;
    }

    /**
     * Create a question for the given state with randomized answer choices
     *
     * @param state The state for this question
     * @return QuizQuestion with 3 randomized choices
     */
    private QuizQuestion createQuestionForState(StateItem state) {
        // StateItem already has 3 cities built in
        String[] choices = new String[] {
                state.getCapitalCity(),
                state.getCity2(),
                state.getCity3()
        };

        // Randomize order
        List<String> choiceList = new ArrayList<>();
        choiceList.add(choices[0]);
        choiceList.add(choices[1]);
        choiceList.add(choices[2]);
        Collections.shuffle(choiceList, random);

        String[] randomizedChoices = choiceList.toArray(new String[3]);

        // Create QuizQuestion with StateItem and randomized choices
        return new QuizQuestion(state, randomizedChoices);
    }

    /**
     * Record user's answer and update quiz score
     *
     * @param question The question being answered
     * @param answer User's selected answer
     */
    public void recordAnswer(QuizQuestion question, String answer) {
        if (currentQuiz == null) {
            Log.e(TAG, "No active quiz");
            return;
        }

        currentQuiz.incrementQuestionsAnswered();

        if (question.isCorrect(answer)) {
            currentQuiz.incrementScore();
            Log.d(TAG, "Correct answer! Score: " + currentQuiz.getScore());
        } else {
            Log.d(TAG, "Wrong answer. Correct was: " + question.getCorrectAnswer());
        }
    }

    /**
     * Get the generated questions for this quiz
     *
     * @return List of 6 QuizQuestions, or null if quiz not created yet
     */
    public List<QuizQuestion> getQuestions() {
        return currentQuestions;
    }

    /**
     * Get a specific question by index (0-based)
     *
     * @param index Question index (0-5)
     * @return QuizQuestion, or null if invalid index
     */
    public QuizQuestion getQuestion(int index) {
        if (currentQuestions != null && index >= 0 && index < currentQuestions.size()) {
            return currentQuestions.get(index);
        }
        return null;
    }

    /**
     * Get current quiz
     *
     * @return Quiz object, or null if not created
     */
    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    /**
     * Get final score
     *
     * @return Score (0-6)
     */
    public int getFinalScore() {
        return currentQuiz != null ? currentQuiz.getScore() : 0;
    }

    /**
     * Check if quiz is complete
     *
     * @return true if all 6 questions answered
     */
    public boolean isQuizComplete() {
        return currentQuiz != null && currentQuiz.isComplete();
    }

    /**
     * Get number of questions per quiz
     *
     * @return 6
     */
    public int getQuestionCount() {
        return QUESTIONS_PER_QUIZ;
    }

    /**
     * Reset manager for new quiz
     */
    public void reset() {
        currentQuiz = null;
        currentQuestions = null;
        Log.d(TAG, "QuizManager reset");
    }
}