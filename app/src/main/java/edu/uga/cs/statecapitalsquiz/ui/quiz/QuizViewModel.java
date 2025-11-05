package edu.uga.cs.statecapitalsquiz.ui.quiz;

import androidx.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.models.QuizQuestion;
import edu.uga.cs.statecapitalsquiz.models.StateItem;
import edu.uga.cs.statecapitalsquiz.utils.QuizManager;

/**
 * QuizViewModel (UPDATED VERSION - Compatible with actual data models)
 *
 * <p><b>Purpose:</b> Manages quiz state across configuration changes and fragment transitions.
 * Integrates with QuizManager to provide real quiz data to UI.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Store current quiz and questions</li>
 *   <li>Track user selections (questionNumber → answerIndex mapping)</li>
 *   <li>Initialize quiz with real state data from database</li>
 *   <li>Provide questions to ViewPager fragments</li>
 * </ul>
 *
 * @author StateCapitalsQuiz Team
 * @version 2.0
 */
public class QuizViewModel extends ViewModel {

    // === ORIGINAL PARTNER CODE (Selection tracking) ===
    private final Map<Integer, Integer> selections = new HashMap<>();
    private final Map<Integer, String> userAnswers = new HashMap<>();

    /**
     * Store user's choice for a question
     *
     * @param questionNumber 1-based question number (1-6)
     * @param choice 1=first radio, 2=second, 3=third, 0=none
     */
    public void setSelection(int questionNumber, int choice) {
        selections.put(questionNumber, choice);

        // Also store the actual answer text
        QuizQuestion question = getQuestion(questionNumber);
        if (question != null && choice >= 1 && choice <= 3) {
            String[] choices = question.getAnswerChoices();
            userAnswers.put(questionNumber, choices[choice - 1]); // Convert 1-based to 0-based
        }
    }

    /**
     * Retrieve user's choice for a question
     *
     * @param questionNumber 1-based question number (1-6)
     * @return 1, 2, 3, or 0 if none selected
     */
    public int getSelection(int questionNumber) {
        Integer value = selections.get(questionNumber);
        return (value == null) ? 0 : value;
    }

    /**
     * Count how many questions remain unanswered
     *
     * @return number of questions with no selection (0 to 6)
     */
    public int countUnanswered() {
        int total = 6;
        int answered = 0;
        for (int i = 1; i <= total; i++) {
            if (getSelection(i) != 0) {
                answered++;
            }
        }
        return total - answered;
    }

    /**
     * Clear all selections (for new quiz)
     */
    public void clearAll() {
        selections.clear();
        userAnswers.clear();
        currentQuiz = null;
        questions = null;
        quizManager = null;
        quizInitialized = false;
    }

    // === DATABASE INTEGRATION CODE ===

    private Quiz currentQuiz;
    private List<QuizQuestion> questions;
    private QuizManager quizManager;
    private boolean quizInitialized = false;

    /**
     * Initialize a new quiz with state data from database
     *
     * @param allStates List of all states from database
     */
    public void initializeQuiz(List<StateItem> allStates) {
        if (quizInitialized) {
            return;
        }

        quizManager = new QuizManager();
        quizManager.setAllStates(allStates);

        currentQuiz = quizManager.createNewQuiz();
        questions = quizManager.getQuestions();

        quizInitialized = true;
    }

    /**
     * Get question data for a specific question number
     * QuestionFragment calls this to display real state/capital data
     *
     * @param questionNumber 1-based question number (1-6)
     * @return QuizQuestion, or null if not initialized
     */
    public QuizQuestion getQuestion(int questionNumber) {
        if (questions != null && questionNumber > 0 && questionNumber <= questions.size()) {
            return questions.get(questionNumber - 1); // Convert to 0-based index
        }
        return null;
    }

    /**
     * Get all questions for this quiz
     *
     * @return List of 6 QuizQuestions, or null if not initialized
     */
    public List<QuizQuestion> getAllQuestions() {
        return questions;
    }

    /**
     * Get current quiz metadata
     *
     * @return Quiz object, or null if not initialized
     */
    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    /**
     * Record all user answers and calculate final score
     * Call this from ResultsFragment
     *
     * @return Final score (0-6)
     */
    public int calculateScore() {
        if (quizManager == null || questions == null) {
            return 0;
        }

        // Record all answers in QuizManager
        for (int i = 1; i <= questions.size(); i++) {
            String userAnswer = userAnswers.get(i);
            if (userAnswer != null) {
                QuizQuestion question = questions.get(i - 1);
                quizManager.recordAnswer(question, userAnswer);
            }
        }

        return quizManager.getFinalScore();
    }

    /**
     * Check if quiz has been initialized
     *
     * @return true if quiz data loaded
     */
    public boolean isQuizInitialized() {
        return quizInitialized;
    }

    /**
     * Reset for new quiz
     */
    public void resetForNewQuiz() {
        clearAll();
    }
}