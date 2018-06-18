package com.github.codetanzania.feature.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;

import tz.co.codetanzania.R;

/*
 * The utility class used specifically to render the
 * obstructive dialog to indicate to the user when necessary tasks is running in
 * async style or in background.
 *
 * Being obstructive, the dialog can only be used in special occasions which requires
 * the tasks to run into completion or failing
 */
public class ObstructiveProgressDialog {
    // Dialog to show while the task is running
    private Dialog mDialog;

    public ObstructiveProgressDialog(Context ctx) {
        initDialog(ctx);
    }

    private void initDialog(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(R.layout.loader_dialog_content_view);
        mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.setCancelable(false);
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void dispose() {
        dismiss();
        mDialog = null;
    }
}
