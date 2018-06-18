package com.github.codetanzania.feature.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tz.co.codetanzania.R;

public class ProgressBarFragment extends Fragment {

    private static ProgressBarFragment mSelf;

    public static ProgressBarFragment getInstance() {
        if (mSelf == null) {
            mSelf = new ProgressBarFragment();
        }
        return mSelf;
    }

    @Override public View onCreateView(
            LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.frag_progress_bar, group, false);
    }
}
