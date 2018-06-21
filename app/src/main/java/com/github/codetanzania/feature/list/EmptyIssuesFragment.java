package com.github.codetanzania.feature.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tz.co.codetanzania.R;

/**
 * Created by anon on 3/17/17.
 */

public class EmptyIssuesFragment extends Fragment {
    private static final String EMPTY_MSG_INTENT = "empty msg";
    private TextView tvError;

    /**
     * Returns new EmptyIssuesFragment displaying text_empty_issues.
     */
    public static EmptyIssuesFragment getNewInstance() {
        EmptyIssuesFragment instance = new EmptyIssuesFragment();
        return instance;
    }

    /**
     * Pass custom message to display.
     */
    public static EmptyIssuesFragment getNewInstance(int messageRes) {
        EmptyIssuesFragment instance = new EmptyIssuesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EMPTY_MSG_INTENT, messageRes);
        instance.setArguments(bundle);
        return instance;
    }

    @Override public View onCreateView(
            LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_empty, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set custom error message
        if (getArguments() != null) {
            tvError = (TextView) view.findViewById(R.id.tv_EmptyIssues);
            int errorMessage = getArguments().getInt(EMPTY_MSG_INTENT, R.string.text_empty_issues);
            tvError.setText(errorMessage);
        }
    }
}
