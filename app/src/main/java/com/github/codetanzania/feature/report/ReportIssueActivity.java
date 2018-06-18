package com.github.codetanzania.feature.report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.codetanzania.core.api.Open311Api;
import com.github.codetanzania.core.Analytics;
import com.github.codetanzania.feature.BaseAppFragmentActivity;
import com.github.codetanzania.open311.android.library.api.ApiModelConverter;
import com.github.codetanzania.open311.android.library.api.models.ApiServiceRequestGet;
import com.github.codetanzania.open311.android.library.models.Attachment;
import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.open311.android.library.models.Problem;
import com.github.codetanzania.open311.android.library.models.Reporter;
import com.github.codetanzania.open311.android.library.utils.AttachmentUtils;
import com.github.codetanzania.util.LookAndFeelUtils;
import com.github.codetanzania.util.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tz.co.codetanzania.R;

public class ReportIssueActivity extends BaseAppFragmentActivity implements
        SelectLocationFragment.OnSelectLocation,
        IssueCategoryPickerDialog.OnSelectIssueCategory,
        ImageAttachmentFragment.OnRemovePreviewItemClick,
        IssueDetailsFormFragment.OnCacheUserData {

    /* the issue category dialog picker */
    private IssueCategoryPickerDialog mIssueCategoryDialogPicker;

    public static final String TAG_SELECTED_SERVICE = "selected_service";

    private static final String TAG = "ReportIssueActivity";

    /* Optimize view lookup/rendering */
    Toolbar toolbar;

    // key used to set the result flag back to the parent activity
    public static final String SUBMISSION_TICKET = "com.github.codetanzania.SUBMISSION_TICKET";

    // issue id
    private String mSubmissionTicket;

    public static final int REQUEST_IMAGE_CAPTURE = 3;
    public static final int REQUEST_BROWSE_MEDIA_STORE = 4;

    // TODO: Use Object Instead of Map<String, Object>
    private final Map<String, Object> mIssueBody = new HashMap<>();

    // TODO: Get rid of the array list. No need when we have Uri object
    private ArrayList<Object> attachments = new ArrayList<>();

    private Category selectedCategory;

    // private Bitmap optionalBitmapAttachment;

    // uri to the photo item
    // TODO: Uncomment the following line to use a more succinct Java 8 Optional<Uri> type wrapper
    private String mPhotoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LookAndFeelUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.iconPrimary));
        }

        setContentView(R.layout.activity_basic);

        if (savedInstanceState == null) {
            // check if service was passed through the intent
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                Category category = bundle.getParcelable(TAG_SELECTED_SERVICE);
                onServiceTypeSelected(category);
            }
        } else {
            // restore state
            mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "SavedFrag");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.basic_toolbar_layout);
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                bar.setCustomView(R.layout.custom_action_bar);
                bar.setDisplayHomeAsUpEnabled(true);
            }
        }

        displayCurrentStep();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentFragment != null && mCurrentFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "SavedFrag", mCurrentFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.hideSoftInputMethod(this);
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    /* When we receive back result from activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        mCurrentFragment.onActivityResult(requestCode, resultCode, dataIntent);
    }

    // when activity result is received back
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int grantResults[]) {
        mCurrentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // when service is selected
    public void onServiceTypeSelected(Category category) {
        setSelectedServiceType(category);
        // call fetch location to.
        startLocationPickerFragment();
    }

    private void setSelectedServiceType(Category category) {
        // note the service id
        mIssueBody.put("service", category.getId());
        this.selectedCategory = category;
    }

    // the function is invoked to fetch the user location.
    private void startLocationPickerFragment() {
        SelectLocationFragment frag = new SelectLocationFragment();
        setCurrentFragment(R.id.frl_FragmentOutlet, TAG_LOCATION_SERVICE, frag);
    }

    public void changeLocation() {
        onBackPressed();
    }


    private void showOptionalDetails(Category selectedService) {
        Category service = selectedService == null ? this.selectedCategory : selectedService;
        if (service == null) {
            return;
        }

        Object description = mIssueBody.get("description");

        IssueDetailsFormFragment frag = IssueDetailsFormFragment.getNewInstance(
                service.getName(),
                String.valueOf(mIssueBody.get("address")),
                description == null ? null : String.valueOf(description),
                mPhotoUri);

        setCurrentFragment(R.id.frl_FragmentOutlet, frag.getClass().getName(), frag);
    }

    public void doPost(String text) {

        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, R.string.warning_empty_issue_body, Toast.LENGTH_SHORT).show();
            return;
        }

        // put optional attachment (image)
        if (mPhotoUri != null) {
            Attachment attachment = AttachmentUtils.getPicAsAttachment(mPhotoUri);

            attachments = new ArrayList<>(1);
            Map<String, Object> imageAttachment = new HashMap<>();
            imageAttachment.put("name", "Issue_" + (new Date()).getTime());
            imageAttachment.put("caption", text);
            imageAttachment.put("mime", attachment.getMime());
            imageAttachment.put("content", attachment.getContent());
            attachments.add(imageAttachment);
        }

        if (!attachments.isEmpty()) {
            mIssueBody.put("attachments", attachments);
        }

        // set description
        mIssueBody.put("description", text);

        // set reporter
        Reporter reporter = Util.getCurrentReporter(this);
        if (reporter == null) {
            Toast.makeText(this, R.string.warning_no_reporter, Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, String> reporterData = new HashMap<>();
        reporterData.put("name", reporter.getName());
        reporterData.put("phone", reporter.getPhone());
        mIssueBody.put("reporter", reporterData);

        // Prepare the dialog
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.text_opening_ticket, getString(R.string.app_name)));
        dialog.setIndeterminate(true);

        //if (mIssueBody.get("pictureFile") != null) {
            // load picture and post data to the server
        //}

        // do the posting
        new Open311Api.ServiceBuilder(this).build(Open311Api.ServiceRequestEndpoint.class)
                .openTicket(Util.getAuthToken(), mIssueBody)
                .enqueue(getPostIssueCallback(dialog));

        // show the dialog
        dialog.show();

        // hide IME
        Util.hideSoftInputMethod(this);
    }

    private void displayMessage(final Problem problem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_post_issue_success_message, problem.getTicketNumber()));
        builder.setPositiveButton(R.string.dialog_post_issue_pos_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.startPreviewIssueActivity(ReportIssueActivity.this, problem);
                finishWithResult();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ReportIssueActivity.this.finish();
            }
        });
        builder.create().show();
        this.mSubmissionTicket = problem.getTicketNumber();
    }

    private Callback<ResponseBody> getPostIssueCallback(final ProgressDialog dialog) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();

                Log.d(TAG, response.message());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String str = response.body().string();
                        Gson gson = new Gson();
                        ApiServiceRequestGet apiServiceRequest = gson.fromJson(str, ApiServiceRequestGet.class);
                        Problem problem = ApiModelConverter.convert(apiServiceRequest);
                        displayMessage(problem);
                    } catch (IOException ioException) {
                        Log.e(TAG, "The exception is " + ioException.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                // show error message
                Toast.makeText(ReportIssueActivity.this, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void finishWithResult() {
        //TODO: Send details about submission
        Analytics.onIssueSubmitted();

        Intent intent = new Intent();
        // TODO: uncomment the following line to return captured photo
        // intent.setData(mPhotoUri);
        if (mSubmissionTicket == null) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            intent.putExtra(SUBMISSION_TICKET, mSubmissionTicket);
            setResult(Activity.RESULT_OK, intent);
        }

        finish();
    }

    @Override
    public void selectLocation(double lats, double longs, String address) {
        // store current longitude and latitude and then move on to the next step
        // by committing another fragment
        mIssueBody.put("address", address == null ? "Unknown Address" : address);

        Map<String, Double[]> location = new HashMap<>();
        location.put("coordinates", new Double[]{longs, lats});
        mIssueBody.put("location", location);
        showOptionalDetails(null);
    }


    @Override
    public void onRemovePreviewItemClicked() {
        if (mCurrentFragment instanceof IssueDetailsFormFragment) {
            ((IssueDetailsFormFragment) mCurrentFragment).removePreviewImageFragment();
            this.mPhotoUri = null;
        }
    }
    @Override
    public void initializeIssueCategoryPickerDialog() {
        // get cached issues
        if (mIssueCategoryDialogPicker == null) {
            mIssueCategoryDialogPicker = new IssueCategoryPickerDialog(this);
        }
        mIssueCategoryDialogPicker.show();
    }

    @Override
    public void onIssueCategorySelected(Category category) {
        setSelectedServiceType(category);
        ((IssueDetailsFormFragment) mCurrentFragment).updateServiceType(category);
    }

    @Override
    public void cacheIssueDescription(@NonNull String description) {
        mIssueBody.put("description", description);
    }

    @Override
    public void clearIssueDescription() {
        mIssueBody.remove("description");
    }

    @Override
    public void cachePhotoUri(@NonNull String photoUri) {
        mPhotoUri = photoUri;
    }

    @Override
    public void clearPhotoUri() {
        mPhotoUri = null;
    }
}
