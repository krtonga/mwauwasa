package com.github.codetanzania.feature.company.faq;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.github.codetanzania.core.model.FAQEntry;
import com.github.codetanzania.feature.company.faq.GroupBy;
import com.github.codetanzania.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tz.co.codetanzania.R;

public class FAQsExpandableListAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "FAQs";

    private Map<String, List<FAQEntry>> mFAQs;
    private List<String> mQuestions;
    private final Context mContext;

    public FAQsExpandableListAdapter(Context ctx, List<FAQEntry> faqEntries) {
        initializeFAQs(faqEntries);
        mContext = ctx;
    }

    // initialize faqs
    private void initializeFAQs(List<FAQEntry> faqEntries) {

        mQuestions = new ArrayList<>();

        Log.d(TAG, "Entries " + faqEntries);

        mFAQs = GroupBy.apply(faqEntries,
           new GroupBy.ValueExtractor<FAQEntry, String>() {
            @Override
            public String extract(FAQEntry in) {
                String question = in.getQuestion();
                mQuestions.add(question);
                return question;
            }
        });
    }

    @Override
    public int getGroupCount() {
        return mFAQs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // each question has one answer
        return mFAQs.get(mQuestions.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mFAQs.get(mQuestions.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mFAQs.get(mQuestions.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // noinspection unchecked
        String qn = ((List<FAQEntry>)getGroup(groupPosition)).get(0).getQuestion();

        if (Util.isNull(convertView)) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_faq_header_item, parent, false);
        }

        TextView tvQn = (TextView) convertView.findViewById(R.id.tv_Question);
        tvQn.setText(qn);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FAQEntry entry = (FAQEntry) getChild(groupPosition, childPosition);

        if (Util.isNull(convertView)) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.view_faq_ans_item, parent, false);
        }

        TextView tvAnswer = (TextView) convertView.findViewById(R.id.tv_Ans);
        tvAnswer.setText(entry.getAnswer());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
