package edu.uga.cs.statecapitalsquiz.ui.quiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * QuizPagerAdapter
 *
 * <p><b>Purpose:</b> A {@link androidx.viewpager2.adapter.FragmentStateAdapter} that supplies
 * one {@link QuestionFragment} per quiz page to the {@link androidx.viewpager2.widget.ViewPager2}.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Report a fixed item count (6 for this project).</li>
 *   <li>Create each {@link QuestionFragment} with its 1-based question number via {@link QuestionFragment#newInstance(int)}.</li>
 * </ul>
 *
 * <p><b>Why FragmentStateAdapter?</b> It correctly handles fragment lifecycle and state
 * when swiping between pages and during configuration changes.</p>
 *
 * <p><b>Future work:</b> The adapter can accept a data model (list of questions with state/cities)
 * to bind real text instead of placeholders.</p>
 */
public class QuizPagerAdapter extends FragmentStateAdapter {
    private final int count;

    public QuizPagerAdapter(@NonNull Fragment parent, int count) {
        super(parent);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // position is 0..count-1
        // For now we pass only the index; later we’ll pass state/capital/options.
        return QuestionFragment.newInstance(position + 1); // 1-based for display
    }

    @Override
    public int getItemCount() {
        return count;
    }
}

