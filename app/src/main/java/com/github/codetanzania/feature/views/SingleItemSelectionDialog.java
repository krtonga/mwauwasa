package com.github.codetanzania.feature.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import tz.co.codetanzania.R;

public class SingleItemSelectionDialog {

    /* A callback to execute when an item is selected */
    public interface OnAcceptSelection {
        void onItemSelected( String item, int position );
    }

    private AlertDialog mAlertDialog;

    private SingleItemSelectionDialog(
       @NonNull AlertDialog alertDialog) {
        mAlertDialog = alertDialog;
    }

    public void open() {
        this.mAlertDialog.show();
    }

    public void cancel() {
        mAlertDialog.cancel();
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }

    public static class Builder implements DialogInterface.OnClickListener {
        private Context mContext;
        private String[] mItems;
        private boolean mCancellable = false;
        private String mTitle;
        private String mSelectedItem;
        private String mActionCancel;
        private String mActionSelect;
        private AlertDialog.OnClickListener   mOnClickListener;
        private AlertDialog.OnCancelListener  mOnCancelListener;
        private AlertDialog.OnDismissListener mOnDismissListener;
        private AlertDialog.OnShowListener    mOnShowListener;
        private OnAcceptSelection             mOnAcceptSelection;

        private Builder(Context mContext) {
            this.mContext = mContext;
        }

        public static Builder withContext(Context ctx) {
            return new Builder(ctx);
        }

        public Builder addItems(@NonNull String...mItems) {
            this.mItems = mItems;
            return this;
        }

        public Builder addItems(@ArrayRes int arrItemsResId) {
            return addItems(mContext.getResources().getStringArray(arrItemsResId));
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setTitle(@StringRes int titleId) {
            return setTitle(mContext.getString(titleId));
        }

        public Builder setSelectedItem(String selectedItem) {
            this.mSelectedItem = selectedItem;
            return this;
        }

        public Builder setCancellable(boolean mCancellable) {
            this.mCancellable = mCancellable;
            return this;
        }

        public Builder setOnActionListener(
            @NonNull AlertDialog.OnClickListener mOnClickListener) {
            this.mOnClickListener = mOnClickListener;
            return this;
        }

        public Builder setActionCancelText(
            @NonNull String actionCancelText) {
            this.mActionCancel = actionCancelText;
            return this;
        }

        public Builder setActionCancelText(
            @StringRes int actionCancelText) {
            return setActionCancelText(mContext.getString(actionCancelText));
        }

        public Builder setActionSelectText(
            @NonNull String actionSelectText) {
            this.mActionSelect = actionSelectText;
            return this;
        }

        public Builder setActionSelectText(
            @StringRes int actionSelectText) {
            return setActionSelectText(mContext.getString(actionSelectText));
        }

        public Builder setOnAcceptSelection(OnAcceptSelection mOnAcceptSelection) {
            this.mOnAcceptSelection = mOnAcceptSelection;
            return this;
        }

        public Builder setOnCancelListener(
            AlertDialog.OnCancelListener mOnCancelListener) {
            this.mOnCancelListener = mOnCancelListener;
            return this;
        }

        public Builder setOnDismissListener(
            AlertDialog.OnDismissListener mOnDismissListener) {
            this.mOnDismissListener = mOnDismissListener;
            return this;
        }

        public Builder setOnShowListener(
            AlertDialog.OnShowListener mOnShowListener) {
            this.mOnShowListener = mOnShowListener;
            return this;
        }

        public SingleItemSelectionDialog build() {
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(mContext);

            // empty mTitle check
            if (!TextUtils.isEmpty(mTitle)) {
                alertDialogBuilder.setTitle(mTitle);
            }

            // check if we've any previous selected item
            int selectedItemIndex = -1, currIndex = 0;
            for (String item : mItems) {
                if (item.equals(mSelectedItem)) {
                    selectedItemIndex = currIndex;
                    break;
                }
                currIndex++;
            }

            // decide which item is selected by default
            if (selectedItemIndex != -1) {
                alertDialogBuilder.setSingleChoiceItems(
                        mItems, selectedItemIndex, this);
            } else {
                alertDialogBuilder.setSingleChoiceItems(
                        mItems, 0, this);
            }

            // optional
            alertDialogBuilder.setCancelable(mCancellable);
            alertDialogBuilder.setOnDismissListener(mOnDismissListener);
            alertDialogBuilder.setOnCancelListener(mOnCancelListener);

            // actions
            String action = TextUtils.isEmpty(mActionCancel) ?
                    mContext.getString(R.string.text_cancel) :
                    mActionCancel;
            alertDialogBuilder.setNegativeButton(action, null);
            action = TextUtils.isEmpty(mActionSelect) ?
                    mContext.getString(R.string.text_choose) :
                    mActionSelect;
            alertDialogBuilder.setPositiveButton(action, mOnClickListener);

            AlertDialog alertDialog = alertDialogBuilder.create();
            /* FIXME: find out why Android API does not include `setOnShowListener()` method in the `AlertDialog.Builder` Companion Object*/
            alertDialog.setOnShowListener(mOnShowListener);

            // return SingleItemSelectionDialog
            return new SingleItemSelectionDialog(alertDialog);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mOnAcceptSelection != null) {
                String selectedItem = mItems[which];
                mOnAcceptSelection.onItemSelected(selectedItem, which);
            }
        }
    }
}
