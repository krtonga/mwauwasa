package com.github.codetanzania.feature.report;

import android.app.Activity;
import android.widget.Toast;

import com.github.codetanzania.open311.android.library.api.CategoriesManager;
import com.github.codetanzania.open311.android.library.db.DatabaseHelper;
import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.feature.views.OnSpinnerItemClick;
import com.github.codetanzania.feature.views.SpinnerDialog;
import com.github.codetanzania.util.Util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import tz.co.codetanzania.R;

public class IssueCategoryPickerDialog implements OnSpinnerItemClick {
    private Activity mActivity;
    private List<Category> mItems;
    private SpinnerDialog mSpinnerDialog;
    private final OnSelectIssueCategory mSelectionCallback;

    public IssueCategoryPickerDialog(OnSelectIssueCategory selectionCallback) {
        mSelectionCallback = selectionCallback;
        initDialogComponents();
    }

    private void initDialogComponents() {
        try {
            mActivity = (Activity) mSelectionCallback;
        } catch (ClassCastException cce) {
            throw new ClassCastException(String.format("%s must be implemented by an Activity class",
                    mSelectionCallback.getClass().getName()));
        }
        if (mActivity == null) {
            return;
        }

        // First attempt to get categories from db
        DatabaseHelper helper = new DatabaseHelper(mActivity.getBaseContext());
        helper.getCategories(onCategoriesRetrievedFromDb(), onError(), false);
    }

    public void show() {
        if(mSpinnerDialog != null) {
            mSpinnerDialog.showSpinnerDialog();
        }
    }

    @Override
    public void onClick(String s, int i) {
        mSelectionCallback.onIssueCategorySelected(mItems.get(i));
    }

    private Consumer<List<Category>> onCategoriesRetrievedFromDb() {
        return new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) throws Exception {
                mItems = categories;
                if (mItems == null || mItems.isEmpty()) {
                    onNoCategoriesFound();
                } else {
                    String text = mActivity.getString(R.string.action_select_issue_category);
                    mSpinnerDialog = new SpinnerDialog(mActivity, getCategoryNames(), text);
                    mSpinnerDialog.bindOnSpinnerListener(IssueCategoryPickerDialog.this);
                }
            }
        };
    }

    private ArrayList<String> getCategoryNames() {
        ArrayList<String> services = new ArrayList<>();
        for (Category service : mItems) {
            services.add(service.getName());
        }
        return services;
    }

    private Consumer<Throwable> onError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable error) throws Exception {
                onNoCategoriesFound();
            }
        };
    }

    private void onNoCategoriesFound() {
        if (!Util.isNetworkAvailable(mActivity)) {
            Toast.makeText(mActivity, R.string.error_no_network_message, Toast.LENGTH_LONG).show();
        } else {
            // make network call to get categories
            CategoriesManager manager = new CategoriesManager(mActivity.getBaseContext());
            manager.getCategories();
        }
    }

    public interface OnSelectIssueCategory {
        void initializeIssueCategoryPickerDialog();
        void onIssueCategorySelected(Category service);
    }
}
