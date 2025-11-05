package edu.uga.cs.statecapitalsquiz.tasks;

import android.util.Log;
import java.util.List;

import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.StateItem;

/**
 * AsyncTask to load states from database.
 */
public class LoadStatesTask extends AsyncTask<Void, Void, List<StateItem>> {

    private static final String TAG = "LoadStatesTask";
    private QuizData quizData;
    private OnStatesLoadedListener listener;

    public LoadStatesTask(QuizData quizData, OnStatesLoadedListener listener) {
        this.quizData = quizData;
        this.listener = listener;
    }

    @Override
    protected List<StateItem> doInBackground(Void... params) {
        try {
            quizData.open();
            List<StateItem> states = quizData.getAllStates();
            quizData.close();
            return states;
        } catch (Exception e) {
            Log.e(TAG, "Error loading states", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<StateItem> states) {
        if (listener != null) {
            listener.onStatesLoaded(states);
        }
    }

    public interface OnStatesLoadedListener {
        void onStatesLoaded(List<StateItem> states);
    }
}
