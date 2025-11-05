package edu.uga.cs.statecapitalsquiz.tasks;

import android.util.Log;

import java.util.List;

import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

/**
 * AsyncTask to load all completed quizzes from database.
 * Runs on background thread to avoid blocking UI.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class LoadQuizzesTask extends AsyncTask<Void, Void, List<Quiz>> {

    private static final String TAG = "LoadQuizzesTask";

    private QuizData quizData;
    private OnQuizzesLoadedListener listener;

    /**
     * Constructor
     *
     * @param quizData Database operations object
     * @param listener Callback for when quizzes are loaded
     */
    public LoadQuizzesTask(QuizData quizData, OnQuizzesLoadedListener listener) {
        this.quizData = quizData;
        this.listener = listener;
    }

    /**
     * Background operation - retrieve all completed quizzes
     */
    @Override
    protected List<Quiz> doInBackground(Void... params) {
        try {
            Log.d(TAG, "Loading past quizzes");

            List<Quiz> quizzes = quizData.getAllCompletedQuizzes();

            Log.d(TAG, "Loaded " + quizzes.size() + " past quizzes");
            return quizzes;

        } catch (Exception e) {
            Log.e(TAG, "Error loading quizzes", e);
            return null;
        }
    }

    /**
     * Called on UI thread after background work completes
     */
    @Override
    protected void onPostExecute(List<Quiz> quizzes) {
        if (listener != null) {
            listener.onQuizzesLoaded(quizzes);
        }
    }

    /**
     * Callback interface for quizzes loading completion
     */
    public interface OnQuizzesLoadedListener {
        void onQuizzesLoaded(List<Quiz> quizzes);
    }
}