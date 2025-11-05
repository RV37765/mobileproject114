package edu.uga.cs.statecapitalsquiz.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.StateItem;
import edu.uga.cs.statecapitalsquiz.tasks.LoadStatesTask;
import edu.uga.cs.statecapitalsquiz.utils.CSVParser;
import java.util.List;

/**
 * SplashFragment
 *
 * <p><b>Purpose:</b> The entry screen that briefly describes the app. In this project,
 * it shows a simple splash layout and provides a “Continue” button to navigate to {@code HomeFragment}.
 * Keeping splash logic in its own fragment makes the flow explicit and testable.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Inflate {@code fragment_splash.xml} (title, subtitle, progress indicator, Continue button).</li>
 *   <li>On button press, call the {@code action_splash_to_home} action in the navigation graph.</li>
 * </ul>
 *
 * <p><b>Why a fragment and not an Activity?</b> We use a single-Activity architecture.
 * Putting splash into a fragment keeps navigation consistent and lets the ActionBar be managed uniformly.</p>
 */
public class SplashFragment extends Fragment {

    private static final String TAG = "SplashFragment";
    private QuizData quizData;
    private Button btnContinue;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnContinue = view.findViewById(R.id.btnContinue);
        progressBar = view.findViewById(R.id.progressBar);

        quizData = new QuizData(requireContext());
        
        btnContinue.setEnabled(false);
        btnContinue.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_splash_to_home));

        loadStatesData();
    }

    private void loadStatesData() {
        Log.d(TAG, "Loading states");
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        LoadStatesTask task = new LoadStatesTask(quizData,
            new LoadStatesTask.OnStatesLoadedListener() {
                @Override
                public void onStatesLoaded(List<StateItem> states) {
                    if (states == null || states.isEmpty()) {
                        Log.d(TAG, "No states, loading CSV");
                        loadFromCSV();
                    } else {
                        Log.d(TAG, "Loaded " + states.size() + " states");
                        onDataReady(states.size());
                    }
                }
            });
        task.execute();
    }

    private void loadFromCSV() {
        try {
            CSVParser parser = new CSVParser(requireContext());
            List<StateItem> states = parser.parseCSV();

            if (states == null || states.isEmpty()) {
                Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_LONG).show();
                return;
            }

            quizData.open();
            for (StateItem state : states) {
                quizData.insertState(state);
            }
            quizData.close();

            Toast.makeText(requireContext(), "Loaded " + states.size() + " states", Toast.LENGTH_SHORT).show();
            onDataReady(states.size());
        } catch (Exception e) {
            Log.e(TAG, "Error loading CSV", e);
            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void onDataReady(int count) {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (btnContinue != null) {
            btnContinue.setEnabled(true);
            btnContinue.setText("Continue (" + count + " states loaded)");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (quizData != null) quizData.close();
    }
}
