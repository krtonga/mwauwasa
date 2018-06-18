package com.github.codetanzania.feature;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.codetanzania.util.LookAndFeelUtils;

import tz.co.codetanzania.R;

public class BaseAppFragmentActivity extends AppCompatActivity {

    protected static final String TAG_LOCATION_SERVICE = "location-service";

    protected Fragment mCurrentFragment;

    protected String [] submission_steps;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (savedInstanceState != null) {
            // restore the fragment
            // restoring is just a matter of requesting the fragment from fragment manager
            // mCurrentFragmentTag = savedInstanceState.getString(TAG_NAME);
            // mContainerId = savedInstanceState.getInt(CONTAINER_ID);
            // mCurrentFragment = getSupportFragmentManager()
            //        .getFragment(savedInstanceState, mCurrentFragmentTag);
            // set current fragment
        //} else {
            final FragmentManager fragManager = getSupportFragmentManager();
            fragManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (fragManager.getBackStackEntryCount() == 0) {
                        finish();
                    } else {
                        displayCurrentStep();
                    }
                }
            });
        //}
    }

    /*@Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // remember the current fragment's tag
        outState.putString(TAG_NAME, mCurrentFragmentTag);
        // remember the container _id
        outState.putInt(CONTAINER_ID, mContainerId);
        // save the fragment instance
        getSupportFragmentManager().putFragment(outState, mCurrentFragmentTag, mCurrentFragment);
    }*/

    protected void setCurrentFragment(
            int containerId, @NonNull String fragTag, @NonNull Fragment frag) {

        FragmentManager fragManager = getSupportFragmentManager();
        // no fragment? (first time we're adding)
        Fragment oldFrag = fragManager.findFragmentById(containerId);
        if (oldFrag == null) {
            // add the fragment for the first time
            fragManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(containerId, frag, fragTag)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else {
            // replace the existing fragment
            fragManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .hide(oldFrag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(containerId, frag, fragTag)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }

        // store the current fragment
        mCurrentFragment = frag;
    }

    protected void removeFragment(@NonNull Fragment frag) {
        FragmentManager fragManager = getSupportFragmentManager();
        fragManager.beginTransaction()
                .remove(frag)
                .commitAllowingStateLoss();
    }

    protected void showNetworkError(
            String msg, String btnCancelText, String btnConfirmText, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(msg).setNegativeButton(btnCancelText, null).setPositiveButton(btnConfirmText, clickListener)
                .create().show();
    }

    protected void showNetworkError(DialogInterface.OnClickListener clickListener) {
        showNetworkError(
                getString(R.string.msg_network_error),
                getString(R.string.text_cancel),
                getString(R.string.text_retry),
                clickListener
        );
    }

    // TODO this should be done by the fragment, rather than here
    protected void displayCurrentStep() {
        final FragmentManager fragManager = getSupportFragmentManager();
        int index = fragManager.getBackStackEntryCount() - 1;
        if (submission_steps == null) {
            submission_steps = getResources().getStringArray(R.array.submission_steps);
        }
        String currentStep = submission_steps[index];
        Toolbar toolbar = (Toolbar) findViewById(R.id.basic_toolbar_layout);
        if (toolbar != null) {
            LookAndFeelUtils.setupActionBar(this, toolbar, currentStep, true);
        }
    }
}
