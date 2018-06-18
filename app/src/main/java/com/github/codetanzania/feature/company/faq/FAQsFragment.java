package com.github.codetanzania.feature.company.faq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.codetanzania.core.model.FAQEntry;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

public class FAQsFragment extends Fragment {

    private static final String FAQS_ARGS_EXTRAS = "faqs-items";

    public static FAQsFragment newInstance(List<FAQEntry> faqEntries) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(FAQS_ARGS_EXTRAS, (ArrayList<FAQEntry>) faqEntries);
        FAQsFragment frag = new FAQsFragment();
        frag.setArguments(args);
        return frag;
    }

    private void initializeFAQS(final ExpandableListView expandableListView) {
        List<FAQEntry> faqEntries = getArguments()
                .getParcelableArrayList(FAQS_ARGS_EXTRAS);
        // expandable adapter
        FAQsExpandableListAdapter adapter = new FAQsExpandableListAdapter(
                getActivity(), faqEntries);
        expandableListView.setAdapter(adapter);

        // allow only one item to expand at a time
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

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_faqs, parent, false);
    }

    @Override
    public void onViewCreated(View fragView, Bundle savedInstanceState) {

        // expandable list view
        ExpandableListView expandableListView =
                (ExpandableListView) fragView.findViewById(R.id.expandable_faqs);

        initializeFAQS(expandableListView);
    }
}
