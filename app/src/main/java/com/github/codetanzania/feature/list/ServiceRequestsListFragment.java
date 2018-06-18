package com.github.codetanzania.feature.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.codetanzania.open311.android.library.models.Problem;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

/* Singleton fragment */
public class ServiceRequestsListFragment extends Fragment {

    public static final String SERVICE_REQUESTS = "SERVICE_REQUESTS";

    // used by the logcat
    private static final String TAG = "ServiceReqFrag";

    // instance to the click listener will be passed along
    // to the RecyclerView's adapter
    private OnItemClickListener<Problem> mClickListener;
    private LinearLayoutManager mLayoutManager;
    //private PaginationInterface mPaginator;

    private RecyclerView rvServiceRequests;
    private List<Problem> mRequests;

    // singleton method
    public static ServiceRequestsListFragment getNewInstance(ArrayList<Problem> requests) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(
                ServiceRequestsListFragment.SERVICE_REQUESTS, requests);
        ServiceRequestsListFragment instance = new ServiceRequestsListFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context theContext) {
        super.onAttach(theContext);
        // cast context... it must implement so!
        try {
            mClickListener = (OnItemClickListener<Problem>) getActivity();
            //mPaginator = (PaginationInterface) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnItemClickListener and PaginationInterface");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_issue_tickets_group, parent, false);
    }

    @Override
    public void onViewCreated(
        View view, Bundle savedInstanceState) {
        rvServiceRequests = (RecyclerView)
                view.findViewById(R.id.rv_ServiceRequests);

        mRequests = getArguments()
                .getParcelableArrayList(SERVICE_REQUESTS);
        bindServiceRequests(mRequests);
    }

    private void bindServiceRequests(List<Problem> serviceRequests) {

        Log.d(TAG, "=======================SERVICE REQUESTS=========================");
        Log.d(TAG, String.valueOf(serviceRequests));
        Log.d(TAG, "======================/SERVICE REQUESTS=========================");

        ServiceRequestsListAdapter adapter = new ServiceRequestsListAdapter(
                getActivity(), getString(R.string.text_issue_tickets), serviceRequests, mClickListener);

        rvServiceRequests.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvServiceRequests.setLayoutManager(mLayoutManager);
        rvServiceRequests.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        rvServiceRequests.setHasFixedSize(true);
        rvServiceRequests.setNestedScrollingEnabled(true);
    }
}
