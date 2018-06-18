package com.github.codetanzania.feature.company.payment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.codetanzania.core.model.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tz.co.codetanzania.R;

public class PaymentInstructionsFragment extends Fragment {

    private static final String PAYMENT_INSTRUCTIONS = "payment methods";

    public static PaymentInstructionsFragment newInstance(ArrayList<PaymentMethod> methods) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(PAYMENT_INSTRUCTIONS, methods);
        PaymentInstructionsFragment frag = new PaymentInstructionsFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override public View onCreateView(
            LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState ) {
        return inflater.inflate(R.layout.frag_payment_instructions, parent, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_instructions);
        List<PaymentMethod> paymentMethods = getArguments().getParcelableArrayList(PAYMENT_INSTRUCTIONS);

        PaymentInstructionsListAdapter adapter = new PaymentInstructionsListAdapter(getActivity(), paymentMethods);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            // Keep track of previous expanded parent
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                // Collapse previous parent if expanded.
                if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
    }
}
