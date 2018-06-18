package com.github.codetanzania.feature.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.codetanzania.open311.android.library.models.Problem;

import java.util.ArrayList;

import tz.co.codetanzania.R;

/**
 * This fragment contains a view pager which can be used to switch between "all",
 * "open" and "closed" issues.
 */
public class ServiceRequestsTabFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ArrayList<Problem> mServiceRequests;
    private IssueListPagerAdapter mAdapter;

    public static ServiceRequestsTabFragment getNewInstance(ArrayList<Problem> requests) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(
                ServiceRequestsListFragment.SERVICE_REQUESTS, requests);
        ServiceRequestsTabFragment instance = new ServiceRequestsTabFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_issues_tabs, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_TicketsActivity);
        mServiceRequests = getArguments()
                .getParcelableArrayList(ServiceRequestsListFragment.SERVICE_REQUESTS);
        createAdapter(mServiceRequests);

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void createAdapter(ArrayList<Problem> newRequests){
        mAdapter = new IssueListPagerAdapter(
                getActivity(),
                getChildFragmentManager(),
                mServiceRequests);

        mViewPager.setAdapter(mAdapter);
    }
}
