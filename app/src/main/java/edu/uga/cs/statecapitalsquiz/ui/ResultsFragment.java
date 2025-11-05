package edu.uga.cs.statecapitalsquiz.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.tasks.UpdateQuizTask;
import edu.uga.cs.statecapitalsquiz.ui.quiz.QuizViewModel;

/**
 * ResultsFragment - Displays final quiz score and saves to database
 */
public class ResultsFragment extends Fragment {

    private static final String TAG = "ResultsFragment";
    private TextView tvScore, tvMessage;
    private Button btnNewQuiz, btnViewHistory;
    private QuizViewModel quizViewModel;
    private QuizData quizData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvScore = view.findViewById(R.id.tvScore);
        tvMessage = view.findViewById(R.id.tvMessage);
        btnNewQuiz = view.findViewById(R.id.btnNewQuiz);
        btnViewHistory = view.findViewById(R.id.btnViewHistory);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        quizData = new QuizData(requireContext());

        calculateAndDisplay();

        btnNewQuiz.setOnClickListener(v -> {
            quizViewModel.resetForNewQuiz();
            Navigation.findNavController(v).navigate(R.id.action_results_to_quizContainer);
        });

        btnViewHistory.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_results_to_history));
    }

    private void calculateAndDisplay() {
        int score = quizViewModel.calculateScore();
        tvScore.setText(score + "/6");

        String message;
        if (score == 6) message = "Perfect! 🌟";
        else if (score >= 4) message = "Great job! 👏";
        else if (score >= 2) message = "Good effort! 📚";
        else message = "Keep trying! 💪";
        tvMessage.setText(message);

        saveToDatabase(score);
    }

    private void saveToDatabase(int score) {
        Quiz quiz = quizViewModel.getCurrentQuiz();
        if (quiz == null) {
            Log.e(TAG, "No quiz to save");
            return;
        }

        quiz.setScore(score);
        quiz.setQuestionsAnswered(6);

        UpdateQuizTask task = new UpdateQuizTask(quizData, quiz,
            new UpdateQuizTask.OnQuizUpdatedListener() {
                @Override
                public void onQuizUpdated(long quizId) {
                    if (quizId != -1) {
                        Log.d(TAG, "Quiz saved: " + quizId);
                        Toast.makeText(requireContext(), "Quiz saved!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        task.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (quizData != null) quizData.close();
    }
}
