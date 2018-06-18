package com.github.codetanzania.feature.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tz.co.codetanzania.R;

/**
 * Created by anon on 3/17/17.
 */

public class EmptyIssuesFragment extends Fragment {

    public static EmptyIssuesFragment getNewInstance(@Nullable Bundle args) {
        EmptyIssuesFragment instance = new EmptyIssuesFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override public View onCreateView(
            LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_empty_issue_tickets, viewGroup, false);
    }
}
