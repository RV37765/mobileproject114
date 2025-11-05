package edu.uga.cs.statecapitalsquiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uga.cs.statecapitalsquiz.R;

/**
 * HelpFragment
 *
 * <p><b>Purpose:</b> Shows rules/instructions for the quiz:
 * 6 questions, 3 choices each, swipe to move, and score displayed at the end.</p>
 *
 * <p><b>Current state:</b> Placeholder text only.</p>
 *
 * <p><b>Future work:</b> Add illustrations, an FAQ, or a short “how it works” section.
 * Could link out to accessibility settings or theme toggles if needed.</p>
 */
public class HelpFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}