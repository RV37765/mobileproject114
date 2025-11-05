package edu.uga.cs.statecapitalsquiz.tasks;

import android.util.Log;
import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

/**
 * AsyncTask to update quiz progress (score and questions answered).
 */
public class UpdateQuizTask extends AsyncTask<Void, Void, Long> {

    private static final String TAG = "UpdateQuizTask";
    private QuizData quizData;
    private Quiz quiz;
    private OnQuizUpdatedListener listener;

    public UpdateQuizTask(QuizData quizData, Quiz quiz, OnQuizUpdatedListener listener) {
        this.quizData = quizData;
        this.quiz = quiz;
        this.listener = listener;
    }

    @Override
    protected Long doInBackground(Void... params) {
        try {
            Log.d(TAG, "Updating quiz " + quiz.getId());
            
            quizData.open();
            quizData.updateQuizProgress(quiz.getId(), quiz.getScore(), quiz.getQuestionsAnswered());
            quizData.close();
            
            Log.d(TAG, "Quiz updated");
            return (long) quiz.getId();

        } catch (Exception e) {
            Log.e(TAG, "Error updating quiz", e);
            return -1L;
        }
    }

    @Override
    protected void onPostExecute(Long quizId) {
        if (listener != null) {
            listener.onQuizUpdated(quizId);
        }
    }

    public interface OnQuizUpdatedListener {
        void onQuizUpdated(long quizId);
    }
}
