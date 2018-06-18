package com.github.codetanzania.feature.company.newconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.codetanzania.open311.android.library.EventHandler;
import com.github.codetanzania.open311.android.library.api.ReportService;
import com.github.codetanzania.open311.android.library.db.DatabaseHelper;
import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.feature.views.ObstructiveProgressDialog;
import com.github.codetanzania.feature.views.TextDrawable;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.util.Util;

import java.util.List;

import io.reactivex.functions.Consumer;
import tz.co.codetanzania.R;

public class RequestNewConnectionFragment extends Fragment implements View.OnClickListener, Problem.Builder.InvalidCallbacks {
    private Category mNewConnectionCategory;

    private TextInputEditText etApplicantFullName;
    private TextInputEditText etApplicantPhoneNumber;
    private TextInputEditText etApplicantEmail;
    private TextInputEditText etApplicantWard;
    private TextInputEditText etApplicantStreet;

    private Button btnSubmitRequest;
    private ObstructiveProgressDialog mLoader;

    public static RequestNewConnectionFragment newInstance() {
        return new RequestNewConnectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // Get "New Connection" category
        DatabaseHelper helper = new DatabaseHelper(getContext());
        helper.getCategories(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) throws Exception {
                for (Category category : categories) {
                    //TODO don't hardcode this
                    // NOTE: If New Connection ID changes, this will break!!
                    if ("New Connection".equals(category.getName())) {
                        mNewConnectionCategory = category;
                        return;
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable error) throws Exception {
                //TODO better error handling
                getActivity().finish();
            }
        }, false);
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,ViewGroup parent, Bundle bundle) {
        return inflater.inflate(
                R.layout.frag_request_new_connection, parent, false);
    }

    @Override
    public void onViewCreated(
        View fragView, Bundle savedInstanceState) {
        etApplicantFullName = (TextInputEditText) fragView.findViewById(R.id.et_UserFullName);
        etApplicantPhoneNumber = (TextInputEditText) fragView.findViewById(R.id.et_UserPhone);
        etApplicantEmail  = (TextInputEditText) fragView.findViewById(R.id.et_UserEmail);
        etApplicantWard   = (TextInputEditText) fragView.findViewById(R.id.et_UserWard);
        etApplicantStreet = (TextInputEditText) fragView.findViewById(R.id.et_UserStreet);

        // show saved reporter information
        bindViewData();

        btnSubmitRequest = (Button) fragView.findViewById(R.id.btn_SubmitRequest);
        btnSubmitRequest.setOnClickListener(this);

        registerForPostUpdates();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_request_new_connection, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                showInfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(newConnectionPostedReceiver);

        if (mLoader != null) {
            mLoader.dispose();
        }
    }

    private void registerForPostUpdates() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(newConnectionPostedReceiver,
                new IntentFilter(EventHandler.BROADCAST_REPORT_RECIEVED));
    }

    private BroadcastReceiver newConnectionPostedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(EventHandler.IS_SUCCESS, false)) {
                final Problem problem = intent.getParcelableExtra(EventHandler.PROBLEM_INTENT);

                // Show same alert dialog as Report Issue
                // TODO Eliminate duplication
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.new_connection_next_steps, problem.getTicketNumber(), getString(R.string.app_name)));
                builder.setPositiveButton(R.string.dialog_post_issue_pos_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.startPreviewIssueActivity(getActivity(), problem);
                        getActivity().finish();
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getActivity().finish();
                    }
                });
                builder.create().show();
            } else {
                Toast.makeText(getContext(), getString(R.string.error_new_connection), Toast.LENGTH_LONG).show();
            }
            dismissLoader();
        }
    };

    private void dismissLoader() {
        if (mLoader != null) {
            mLoader.dismiss();
        }
    }

    private void showInfoDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.text_new_connection_info);
        alertDialogBuilder.create().show();
    }

    private void bindViewData() {

        // A fixed part
        String code = getString(R.string.default_area_code);
        etApplicantPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                new TextDrawable("+" + code, null), null, null, null);
        etApplicantPhoneNumber.setCompoundDrawablePadding(
                (code.length() + 1) * 36);

        Reporter user = Util.getCurrentReporter(getActivity());
        if (Util.isNull(user)) {
            etApplicantFullName.requestFocus();
        } else {

            if (!TextUtils.isEmpty(user.getName())) {
                etApplicantFullName.setText(user.getName());
            }

            if (!TextUtils.isEmpty(user.getPhone())) {

                String userPhone = user.getPhone().startsWith(code) ?
                        user.getPhone().substring(code.length()) :
                        user.getPhone();

                etApplicantPhoneNumber.setText(userPhone);
            }

            if (!TextUtils.isEmpty(user.getEmail())) {
                etApplicantEmail.setText(user.getEmail());
            } else {
                etApplicantEmail.requestFocus();
            }
        }
    }

    private String getAddress() {
        String street = etApplicantStreet.getText().toString();
        String ward = etApplicantWard.getText().toString();
        if (TextUtils.isEmpty(street)) {
            etApplicantStreet.setError(getString(R.string.error_street_required));
            return null;
        }
        if (TextUtils.isEmpty(ward)) {
            etApplicantWard.setError(getString(R.string.error_ward_required));
            return null;
        }
        return street +", "+ ward;
    }

    private void clearErrors() {
        etApplicantFullName.setError(null);
        etApplicantPhoneNumber.setError(null);
        etApplicantEmail.setError(null);
        etApplicantWard.setError(null);
        etApplicantStreet.setError(null);
    }

    @Override
    public void onClick(View v) {
        // clear errors so user can start with clean slate
        clearErrors();
        Util.hideSoftInputMethod(getActivity());

        String address = getAddress();
        if (TextUtils.isEmpty(address)) {
            return;
        }

        // build the new service request
        Problem.Builder builder = new Problem.Builder(this);
        builder.setUsername(etApplicantFullName.getText().toString());
        String fullPhoneNumber = getString(R.string.default_area_code)
                + etApplicantPhoneNumber.getText().toString();
        builder.setPhoneNumber(fullPhoneNumber);
        String email = etApplicantEmail.getText().toString();

        if (builder.isValidEmail(email)) {
            builder.setEmail(etApplicantEmail.getText().toString());
        } /*else {
            etApplicantEmail.setError(getString(R.string.error_email_required));
            return;
        }*/

        builder.setCategory(mNewConnectionCategory);
        builder.setAddress(getAddress());
        builder.setLocation(new Location(""));
        builder.setDescription(getString(R.string.new_connection_default_description));
        Problem newServiceRequest = builder.build();

        // attempt to post it. Will send a broadcast on result.
        if (newServiceRequest != null) {
            ReportService.postNewProblem(getContext(), newServiceRequest);
            mLoader = new ObstructiveProgressDialog(getContext());
            mLoader.show();
        }
    }

    @Override
    public void onInvalidUsername() {
        etApplicantFullName.setError(getString(R.string.error_username_required));
    }

    @Override
    public void onInvalidPhoneNumber() {
        etApplicantPhoneNumber.setError(getString(R.string.error_phone_required));
    }

    @Override
    public void onInvalidCategory() {
        // shouldn't occur
    }

    @Override
    public void onInvalidLocation() {
        // shouldn't occur
    }

    @Override
    public void onInvalidAddress() {
        // already done by ward and street verification
    }

    @Override
    public void onInvalidDescription() {
        // shouldn't occur
    }
}
