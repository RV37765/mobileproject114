package edu.uga.cs.statecapitalsquiz.tasks;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

/**
 * Task to create a new quiz with randomly selected states using Executors.
 * Runs on a background thread to avoid blocking the UI.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.1
 */
public class CreateQuizTask {

    private static final String TAG = "CreateQuizTask";

    private final QuizData quizData;
    private final OnQuizCreatedListener listener;

    /**
     * Constructor
     *
     * @param quizData Database operations object
     * @param listener Callback for when quiz is created
     */
    public CreateQuizTask(QuizData quizData, OnQuizCreatedListener listener) {
        this.quizData = quizData;
        this.listener = listener;
    }

    /**
     * Executes the task to create a quiz in the background.
     */
    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Background work
            Quiz quiz = doInBackground();

            // Post result to main thread
            handler.post(() -> onPostExecute(quiz));
        });
    }

    private Quiz doInBackground() {
        try {
            Log.d(TAG, "Creating new quiz");

            // Select 6 random unique state IDs
            int[] stateIds = quizData.selectRandomStates(6);

            // Create quiz in database
            long quizId = quizData.createNewQuiz(stateIds);

            // Retrieve the created quiz
            Quiz quiz = quizData.getQuizById((int) quizId);

            Log.d(TAG, "Quiz created with ID: " + quizId);
            return quiz;

        } catch (Exception e) {
            Log.e(TAG, "Error creating quiz", e);
            return null;
        }
    }

    private void onPostExecute(Quiz quiz) {
        if (listener != null) {
            listener.onQuizCreated(quiz);
        }
    }

    /**
     * Callback interface for quiz creation completion
     */
    public interface OnQuizCreatedListener {
        void onQuizCreated(Quiz quiz);
    }
}
