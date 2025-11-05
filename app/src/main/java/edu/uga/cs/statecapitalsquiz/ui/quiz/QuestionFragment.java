package edu.uga.cs.statecapitalsquiz.ui.quiz;

import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.models.QuizQuestion;

/**
 * QuestionFragment (UPDATED VERSION - Compatible with actual data models)
 *
 * <p><b>Purpose:</b> Displays one quiz question with real state/capital data.
 * Shows state name and 3 randomized city choices from QuizQuestion model.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Get QuizQuestion from ViewModel for this question number</li>
 *   <li>Display state name in prompt</li>
 *   <li>Display 3 city choices from QuizQuestion.getAnswerChoices()</li>
 *   <li>Save/restore user selection via ViewModel</li>
 * </ul>
 *
 * @author StateCapitalsQuiz Team
 * @version 2.0
 */
public class QuestionFragment extends Fragment {

    private static final String TAG = "QuestionFragment";
    private static final String ARG_INDEX = "arg_index";

    public static QuestionFragment newInstance(int questionNumber) {
        QuestionFragment f = new QuestionFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_INDEX, questionNumber);
        f.setArguments(b);
        return f;
    }

    private int questionNumber; // 1-based (1-6)
    private QuizViewModel quizViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        questionNumber = (args != null) ? args.getInt(ARG_INDEX, 1) : 1;

        // Get shared ViewModel
        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvQuestionNumber = view.findViewById(R.id.tvQuestionNumber);
        TextView tvPrompt = view.findViewById(R.id.tvPrompt);
        RadioGroup rgChoices = view.findViewById(R.id.rgChoices);

        RadioButton rb1 = view.findViewById(R.id.rbChoice1);
        RadioButton rb2 = view.findViewById(R.id.rbChoice2);
        RadioButton rb3 = view.findViewById(R.id.rbChoice3);

        tvQuestionNumber.setText("Question " + questionNumber + " of 6");

        // Get real question data from ViewModel
        QuizQuestion question = quizViewModel.getQuestion(questionNumber);

        if (question != null) {
            // Display question text
            tvPrompt.setText(question.getQuestionText());

            // Get the 3 randomized answer choices
            String[] choices = question.getAnswerChoices();
            rb1.setText(choices[0]);
            rb2.setText(choices[1]);
            rb3.setText(choices[2]);

            Log.d(TAG, "Q" + questionNumber + ": " + question.getStateName() +
                    " → choices: " + choices[0] + ", " + choices[1] + ", " + choices[2]);
        } else {
            // Fallback if data not loaded yet
            Log.w(TAG, "Question data not available yet for Q" + questionNumber);
            tvPrompt.setText("Loading question " + questionNumber + "...");
            rb1.setText("Loading...");
            rb2.setText("Loading...");
            rb3.setText("Loading...");
        }

        // Restore saved selection (if any)
        int saved = quizViewModel.getSelection(questionNumber); // 1, 2, 3, or 0
        if (saved == 1) {
            rgChoices.check(R.id.rbChoice1);
        } else if (saved == 2) {
            rgChoices.check(R.id.rbChoice2);
        } else if (saved == 3) {
            rgChoices.check(R.id.rbChoice3);
        } else {
            rgChoices.clearCheck();
        }

        // Save new selections to ViewModel
        rgChoices.setOnCheckedChangeListener((group, checkedId) -> {
            int choice = 0;
            if (checkedId == R.id.rbChoice1) {
                choice = 1;
            } else if (checkedId == R.id.rbChoice2) {
                choice = 2;
            } else if (checkedId == R.id.rbChoice3) {
                choice = 3;
            }

            quizViewModel.setSelection(questionNumber, choice);

            Log.d(TAG, "Q" + questionNumber + ": User selected choice " + choice);
        });
    }
}