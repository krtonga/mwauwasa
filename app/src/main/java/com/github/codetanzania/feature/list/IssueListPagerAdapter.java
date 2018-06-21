package com.github.codetanzania.feature.list;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.open311.android.library.utils.ProblemCollections;
import com.github.codetanzania.feature.list.EmptyIssuesFragment;
import com.github.codetanzania.feature.list.ServiceRequestsListFragment;

import java.util.ArrayList;

import tz.co.codetanzania.R;

/**
 * This manages issue lists, creating a tab for all issues, open issues and closed issues.
 * If it is provided an empty list, it will display only an empty fragment.
 */
public class IssueListPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;

    private ArrayList<Problem> all;
    private ArrayList<Problem> open;
    private ArrayList<Problem> closed;

    private final Context mContext;

    private boolean isEmpty;

    public IssueListPagerAdapter(Context ctx, FragmentManager fm, ArrayList<Problem> requests) {
        super(fm);

        this.mContext = ctx;

        if (requests.isEmpty()) {
            isEmpty = true;
            return;
        }

//        This can be used to mock a CLOSED request for testing purposes on list view
//        ServiceRequest mockRequest = new ServiceRequest();
//        mockRequest.description = "mock desc";
//        mockRequest.service = new Service("B", "Billing", "#FFFFFF");
//        mockRequest.createdAt = new Date(2017, 10, 3, 12, 19);
//        mockRequest.status = new Status(CLOSED, "#000000");
//        requests.add(mockRequest);

        all = new ArrayList<>();
        open = new ArrayList<>();
        closed = new ArrayList<>();

        ProblemCollections.sortByDate(requests);
        for (Problem request : requests) {
            if (request != null) {
                all.add(request);

                if (request.isOpen()) {
                    open.add(request);
                } else {
                    closed.add(request);
                }
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (isEmpty) {
            return EmptyIssuesFragment.getNewInstance();
        }

        switch (position) {
            case 0: return ServiceRequestsListFragment.getNewInstance(all);
            case 1: return ServiceRequestsListFragment.getNewInstance(open);
            case 2: return ServiceRequestsListFragment.getNewInstance(closed);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (isEmpty) {
            return null;
        }
        switch (position) {
            case 0: return mContext.getString(R.string.tab_all_issues);
            case 1: return mContext.getString(R.string.tab_open_issues);
            case 2: return mContext.getString(R.string.tab_closed_issues);
        }
        return null;
    }

    @Override
    public int getCount() {
        return isEmpty ? 1 : NUM_ITEMS;
    }
}
