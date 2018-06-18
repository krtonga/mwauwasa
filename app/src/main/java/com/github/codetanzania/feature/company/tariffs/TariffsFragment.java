package com.github.codetanzania.feature.company.tariffs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.codetanzania.core.model.Tariff;
import com.github.codetanzania.feature.company.contact.ContactUtils;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

public class TariffsFragment extends Fragment {

    private static final String KEY_TARIFFS = "tariffs";
    private RecyclerView mRvTariffs;
    private TextView mFooter;

    public static TariffsFragment newInstance(ArrayList<Tariff> tariffs) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_TARIFFS, tariffs);

        TariffsFragment frag = new TariffsFragment();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState ) {
        return inflater.inflate(R.layout.frag_tariffs, parent, false);
    }

    @Override
    public void onViewCreated(
        View fragView, Bundle savedInstanceState) {
        mFooter = (TextView) fragView.findViewById(R.id.tv_call_for_sewerage);
        mRvTariffs = (RecyclerView) fragView.findViewById(R.id.rv_water_tariffs);
        prepareTariffs(mRvTariffs);

        // on click call company
        mFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactUtils.callCallCenter(getActivity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // color span green
        String callToAction = getString(R.string.tariffs_sewerage_call_for_action);
        String greenWord    = getString(R.string.tariffs_sewerage_call_for_action_green_word);
        int start = callToAction.indexOf(greenWord);
        Spannable wordSpan = new SpannableString(callToAction);
        if(wordSpan.length() != 0 && start != -1) {
            wordSpan.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(getContext(),
                    R.color.green)),
                    start,
                    start + 4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mFooter.setText(wordSpan);
        }
    }

    private void prepareTariffs(RecyclerView tariffsRecyclerView) {
        List<Tariff> tariffs = getArguments().getParcelableArrayList(KEY_TARIFFS);
        if (tariffs == null || tariffs.isEmpty()) {
            return;
        }

        TariffsRecyclerViewAdapter tariffsRecyclerViewAdapter =
                new TariffsRecyclerViewAdapter(tariffs);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

//            HeaderItemDecoration headerItemDecoration = new HeaderItemDecoration(tariffsRecyclerView,
//                    tariffsRecyclerViewAdapter);

        tariffsRecyclerView.setLayoutManager(linearLayoutManager);
        tariffsRecyclerView.setAdapter(tariffsRecyclerViewAdapter);
//            tariffsRecyclerView.addItemDecoration(headerItemDecoration);
        tariffsRecyclerView.setHasFixedSize(true);
    }
}
