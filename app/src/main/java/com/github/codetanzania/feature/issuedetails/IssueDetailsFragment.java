package com.github.codetanzania.feature.issuedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.Constants;
import com.github.codetanzania.open311.android.library.models.ChangeLog;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.util.LocalTimeUtils;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

public class IssueDetailsFragment extends Fragment {

    private static final String TAG = "IssueDetailsFragment";

    // reference to the recycler view. used to show map and image captured when
    // user submitted an issue
    // private RecyclerView mAttachmentsRecyclerView;

    public static IssueDetailsFragment getInstance(Bundle args) {
        IssueDetailsFragment inst = new IssueDetailsFragment();
        inst.setArguments(args);
        return inst;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_issue_details, group, false);
    }

    @Override
    public void onViewCreated(View fragView, Bundle savedInstanceState) {

        Bundle args = getArguments();
        Problem serviceRequest = args.getParcelable(Constants.MajiFix.TICKET);
        assert serviceRequest != null;

        TextView tvIssueLocation = (TextView) fragView.findViewById(R.id.tv_IssueLocationContent);
        TextView tvIssueDate = (TextView) fragView.findViewById(R.id.tv_IssueDate);
        TextView tvIssueCategoryContent = (TextView) fragView.findViewById(R.id.tv_IssueCategoryContent);
        TextView tvIssueContent = (TextView) fragView.findViewById(R.id.tv_IssueContent);
        TextView tvIssueStatus = (TextView) fragView.findViewById(R.id.tv_IssueStatus);

        String location = TextUtils.isEmpty(serviceRequest.getAddress()) ?
                getString(R.string.text_unknown_location) : serviceRequest.getAddress();
        tvIssueLocation.setText(location);

        tvIssueStatus.setText(serviceRequest.getStatus().getName());
        tvIssueDate.setText(LocalTimeUtils.formatShortDate(serviceRequest.getCreatedAt()));
        tvIssueCategoryContent.setText(serviceRequest.getCategory().getName());
        tvIssueContent.setText(serviceRequest.getDescription());

        if (serviceRequest.getChangeLog() != null) {
            List<ChangeLog> publicComments = new ArrayList<>();
            for (ChangeLog log : serviceRequest.getChangeLog()) {
                if (log.isPublic()) {
                    publicComments.add(log);
                }
            }

            IssueProgressTimelineAdapter issueProgressTimelineAdapter = new IssueProgressTimelineAdapter(publicComments);
            RecyclerView statusesRecyclerView = (RecyclerView) fragView.findViewById(R.id.rv_IssueProgress);
            statusesRecyclerView.setAdapter(issueProgressTimelineAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            statusesRecyclerView.setLayoutManager(manager);
        }
    }
}
