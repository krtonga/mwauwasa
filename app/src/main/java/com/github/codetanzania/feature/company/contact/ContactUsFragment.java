package com.github.codetanzania.feature.company.contact;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.codetanzania.core.model.Jurisdiction;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

public class ContactUsFragment extends Fragment
    implements View.OnClickListener, ContactUsRecyclerViewAdapter.OnHandleUserAction {

    private static final String JURISDICTIONS_EXTRA = "jurisdictions";

    public static final int ACTION_TYPE_DIAL = 0;
    public static final int ACTION_TYPE_GET_DIRECTIONS = 1;
    public static final int ACTION_VISIT_WEBSITE = 2;
    public static final int ACTION_TYPE_EMAIL = 3;

    public static Fragment newInstance(ArrayList<Jurisdiction> jurisdictions) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(JURISDICTIONS_EXTRA, jurisdictions);
        ContactUsFragment frag = new ContactUsFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_contact_us, parent, false);
    }

    @Override
    public void onViewCreated(View fragView, Bundle savedInstanceState) {

        // call center
        // Button btnCallCenter = (Button) fragView.findViewById(R.id.btn_CallCenter);
        // btnCallCenter.setOnClickListener(this);

        // jurisdictions
        RecyclerView recyclerView = (RecyclerView) fragView.findViewById(R.id.rv_Jurisdictions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        List<Jurisdiction> jurisdictions = getArguments().getParcelableArrayList(JURISDICTIONS_EXTRA);
        ContactUsRecyclerViewAdapter jurisdictionsRecyclerViewAdapter =
                new ContactUsRecyclerViewAdapter(jurisdictions, this);
        recyclerView.setAdapter(jurisdictionsRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_jurisdiction, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_call){
            ContactUtils.callCallCenter(getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            /*case R.id.btn_CallCenter:
                initiateCall();
                break;*/
        }
    }

    @Override
    public void handleUserAction(int actionType, Bundle userData) {
        switch (actionType) {
            case ACTION_TYPE_EMAIL:
                ContactUtils.initiateEmail(getActivity(), userData);
                break;
            case ACTION_TYPE_DIAL:
                ContactUtils.initiateCall(getActivity(), userData);
                break;
            case ACTION_TYPE_GET_DIRECTIONS:
                ContactUtils.initiateGoogleMap(getActivity(), userData);
                break;
            case ACTION_VISIT_WEBSITE:
                ContactUtils.initiateBrowser(getActivity(), userData);
                break;
            default:
                break;
        }
    }
}
