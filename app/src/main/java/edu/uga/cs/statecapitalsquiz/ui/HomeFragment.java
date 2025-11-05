package edu.uga.cs.statecapitalsquiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uga.cs.statecapitalsquiz.R;

/**
 * HomeFragment
 *
 * <p><b>Purpose:</b> The app’s hub. From here the user can:</p>
 * <ul>
 *   <li>Start a new quiz (navigates to {@code QuizContainerFragment}).</li>
 *   <li>View past results (navigates to {@code HistoryFragment}).</li>
 *   <li>Read help/instructions (navigates to {@code HelpFragment}).</li>
 * </ul>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Inflate {@code fragment_home.xml} with three primary buttons.</li>
 *   <li>Wire button clicks to navigation graph actions:
 *     <ul>
 *       <li>{@code action_home_to_quiz}</li>
 *       <li>{@code action_home_to_history}</li>
 *       <li>{@code action_home_to_help}</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p><b>Future work:</b> This screen can show the last score, “resume quiz” affordance,
 * or app settings. For now it’s a clean, minimal navigation hub.</p>
 */
public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnStartQuiz = view.findViewById(R.id.btnStartQuiz);
        Button btnResults   = view.findViewById(R.id.btnResults);
        Button btnHelp      = view.findViewById(R.id.btnHelp);

        btnStartQuiz.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_quiz));

        btnResults.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_history));

        btnHelp.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_help));
    }
}
