package edu.uga.cs.statecapitalsquiz.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.database.QuizData;
import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.tasks.LoadQuizzesTask;

/**
 * HistoryFragment - Displays past quiz results
 */
public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private QuizData quizData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        quizData = new QuizData(requireContext());
        loadHistory();
    }

    private void loadHistory() {
        LoadQuizzesTask task = new LoadQuizzesTask(quizData,
            new LoadQuizzesTask.OnQuizzesLoadedListener() {
                @Override
                public void onQuizzesLoaded(List<Quiz> quizzes) {
                    if (quizzes == null || quizzes.isEmpty()) {
                        showEmpty();
                    } else {
                        displayQuizzes(quizzes);
                    }
                }
            });
        task.execute();
    }

    private void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    private void displayQuizzes(List<Quiz> quizzes) {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        recyclerView.setAdapter(new HistoryAdapter(quizzes));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (quizData != null) quizData.close();
    }

    static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        private final List<Quiz> quizzes;

        HistoryAdapter(List<Quiz> quizzes) {
            this.quizzes = quizzes;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_quiz_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Quiz quiz = quizzes.get(position);
            holder.tvDate.setText(quiz.getDate());
            holder.tvScore.setText(quiz.getScore() + "/6");
        }

        @Override
        public int getItemCount() {
            return quizzes.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate, tvScore;

            ViewHolder(View itemView) {
                super(itemView);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvScore = itemView.findViewById(R.id.tvScore);
            }
        }
    }
}
