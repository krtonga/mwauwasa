package com.github.codetanzania.feature.report;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.codetanzania.open311.android.library.models.Category;
import com.github.codetanzania.open311.android.library.utils.AttachmentUtils;

import tz.co.codetanzania.R;

public class IssueDetailsFormFragment extends Fragment implements
        PopupMenu.OnMenuItemClickListener,
        DialogInterface.OnClickListener {

    /* request code for accessing device camera */
    private static final int REQUEST_ACCESS_CAMERA = 1;

    /* qualifier key for the selected service */
    private static final String KEY_SELECTED_SERVICE = "selected_service";
    private static final String KEY_SELECTED_LOCATION = "selected_location";
    private static final String KEY_ISSUE_DESCRIPTION = "issue_body";
    private static final String KEY_ISSUE_PHOTO_ITEM  = "photo_item";

    /* reference to the views */
    private ImageButton mBtnAddAttachment;
    private EditText mEtIssueTitle;
    private EditText mEtIssueDescription;
    private View     mAttachmentPreview;
    private Button   mBtnSubmitIssue;
    private EditText mEtIssueLocation;

    /* the fragment used to preview images before uploading to the server */
    private ImageAttachmentFragment mPreviewImageFrag;

    /* A callback to execute when the fragment is destroyed */
    private OnCacheUserData mOnCacheUserData;

    /* the issue category dialog picker */
    private IssueCategoryPickerDialog mIssueCategoryDialogPicker;
    /* notifies the activity when user selects issue category */
    private IssueCategoryPickerDialog
            .OnSelectIssueCategory mOnSelectIssueCategory;

    private String mImagePath;

    /* get new instance */
    public static IssueDetailsFormFragment getNewInstance(String selectedService,
        String selectedLocation, String description, String photoUri) {
        Bundle args = new Bundle();
        args.putString(KEY_SELECTED_SERVICE, selectedService);
        args.putString(KEY_SELECTED_LOCATION, selectedLocation);
        args.putString(KEY_ISSUE_DESCRIPTION, description);
        args.putString(KEY_ISSUE_PHOTO_ITEM, photoUri);
        IssueDetailsFormFragment frag = new IssueDetailsFormFragment();
        frag.setArguments(args);
        return frag;
    }

    public void addPreviewImageFragment(String photoUri, boolean mayRotate) {
        mPreviewImageFrag = ImageAttachmentFragment.getNewInstance(photoUri, mayRotate);
        FragmentManager fragManager  = getChildFragmentManager();
        FragmentTransaction ft       = fragManager.beginTransaction();
        ft.add(R.id.fr_Attachment, mPreviewImageFrag)
            .disallowAddToBackStack()
            .commitAllowingStateLoss();
        mAttachmentPreview.setVisibility(View.VISIBLE);
        mAttachmentPreview.setAlpha(0.0f);
        mAttachmentPreview.animate().alpha(1.0f);
    }

    public void removePreviewImageFragment() {
        if (mPreviewImageFrag != null) {
            FragmentManager fragManager = getChildFragmentManager();
            fragManager.beginTransaction().remove(mPreviewImageFrag).commit();
            mPreviewImageFrag = null;
            mAttachmentPreview.setVisibility(View.GONE);
        }
        getArguments().remove(KEY_ISSUE_PHOTO_ITEM);
    }

    /* request permission to take photos */
    private void askForTakePhotoAndWriteToExternalStoragePermissions() {
        // first, we need to check if we've got permission to access camera and save pictures
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(R.string.title_take_photo_permission)
                    .setMessage(R.string.text_take_photo_permission)
                    .setCancelable(true)
                    .setNegativeButton(R.string.text_cancel, null)
                    .setPositiveButton(R.string.action_allow_take_photo, this);
            alertDialogBuilder.create().show();
        }

        // request permission
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACCESS_CAMERA);
    }

    private boolean hasTakePhotoAndWriteToExternalStoragePermissions() {
        return ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&  ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /* handle user events */
    private void handleUserEvents() {
        mBtnAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // choose where to fetch images,
                // IF user has granted us the permission to both write to storage device and access the camera device
                if (hasTakePhotoAndWriteToExternalStoragePermissions()) {
                    displaySelectImageSrcPopup(v);
                } else {
                    askForTakePhotoAndWriteToExternalStoragePermissions();
                }
            }
        });

        /* handle proper IME scanning and positioning */
        mEtIssueDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEtIssueDescription.requestFocus();
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                return false;
            }
        });

        /* allow user to change issue category */
        mEtIssueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get cached issues
                if (mIssueCategoryDialogPicker == null) {
                    mIssueCategoryDialogPicker = new IssueCategoryPickerDialog(mOnSelectIssueCategory);
                }
                mIssueCategoryDialogPicker.show();
            }
        });

        /* allow user to change location of the problem */
        mEtIssueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReportIssueActivity)getActivity()).changeLocation();
            }
        });

        /* when the submit issue button is clicked */
        mBtnSubmitIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEtIssueDescription.getText().toString();
                ((ReportIssueActivity)getActivity()).doPost(text);
            }
        });
    }

    /* the function to display a popup that allows user to select the src of image */
    private void displaySelectImageSrcPopup(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), anchorView);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_photo_item_src, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    /* bind data to the views */
    private void bindData() {
        String textData = getArguments().getString(KEY_SELECTED_SERVICE);
        mEtIssueTitle.setText(textData);

        String locationData = getArguments().getString(KEY_SELECTED_LOCATION);
        mEtIssueLocation.setText(locationData);

        String descriptionData = getArguments().getString(KEY_ISSUE_DESCRIPTION);

        if (!TextUtils.isEmpty(descriptionData)) {
            mEtIssueDescription.setText(descriptionData);
        }

        mImagePath = getArguments().getString(KEY_ISSUE_PHOTO_ITEM);
        if (mImagePath != null) {
            addPreviewImageFragment(mImagePath, false);
        }
    }

    /* update service type */
    public void updateServiceType(@NonNull Category category) {
        mEtIssueTitle.setText(category.getName());
    }

    /* public update location name */
    public void updateLocationName(@NonNull String locationName) {
        mEtIssueLocation.setText(locationName);
    }

    @Override public void onAttach(Context ctx) {
        super.onAttach(ctx);
        try {
            mOnSelectIssueCategory = (IssueCategoryPickerDialog.OnSelectIssueCategory)ctx;
            mOnCacheUserData = (OnCacheUserData) ctx;
        } catch (ClassCastException cce) {
            throw new ClassCastException(String.format(
                    "%s must implement %s and %s",
                    ctx.getClass().getName(),
                    IssueCategoryPickerDialog.class.getName(),
                    OnCacheUserData.class.getName()
            ));
        }
    }

    @Override public View onCreateView(
        LayoutInflater inflater,
        ViewGroup group,
        Bundle savedInstanceState ) {
        return inflater.inflate(R.layout.frag_issue_description_form, group, false);
    }

    @Override public void onViewCreated (
        View view,
        Bundle savedInstanceState ) {
        mBtnAddAttachment = (ImageButton) view.findViewById(R.id.btn_addImage);
        mEtIssueTitle = (EditText) view.findViewById(R.id.et_IssueTitle);
        mEtIssueDescription = (EditText) view.findViewById(R.id.et_IssueDescription);
        mAttachmentPreview = view.findViewById(R.id.fr_Attachment);
        mBtnSubmitIssue = (Button) view.findViewById(R.id.btn_SubmitIssue);
        mEtIssueLocation = (EditText) view.findViewById(R.id.et_IssueLocation);

        mEtIssueDescription.requestFocus();
        bindData();
        handleUserEvents();
    }

    @Override public void onDestroy() {
        cacheDescription();
        cacheImageUrl();
        super.onDestroy();
    }

    private void cacheDescription() {
        String description = String.valueOf(mEtIssueDescription.getText());
        if (!TextUtils.isEmpty(description)) {
            mOnCacheUserData.cacheIssueDescription(description);
        } else {
            mOnCacheUserData.clearIssueDescription();
        }
    }

    private void cacheImageUrl() {
        if (mImagePath != null) {
            mOnCacheUserData.cachePhotoUri(mImagePath);
        } else {
            mOnCacheUserData.clearPhotoUri();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_photo_item__src_media_store:
                AttachmentUtils.dispatchAddFromGalleryIntent(getActivity());
                break;
            case R.id.item_photo_item__src_camera:
                mImagePath = AttachmentUtils.dipatchTakePictureIntent(getActivity());
                break;
        }
        return false;
    }

    // when activity result is received back
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String permissions[], @NonNull int grantResults[]) {
        // confirm the result code
        if (requestCode == REQUEST_ACCESS_CAMERA && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displaySelectImageSrcPopup(mAttachmentPreview);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mImagePath = AttachmentUtils.setThumbnailFromActivityResult(mBtnAddAttachment, mImagePath,
                requestCode, resultCode, intent);

        if (mImagePath != null) {
            // This is a hack because the library isn't set up correctly for what we need.
            mBtnAddAttachment.setImageDrawable(null);

            addPreviewImageFragment(mImagePath,
                    ReportIssueActivity.REQUEST_IMAGE_CAPTURE == requestCode);
        }
        getArguments().putString(KEY_ISSUE_PHOTO_ITEM, mImagePath);
        cacheImageUrl();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // request permission
       ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACCESS_CAMERA);
    }

    public interface OnCacheUserData {
        void cacheIssueDescription(@NonNull String description);
        void clearIssueDescription();
        void cachePhotoUri(@NonNull String photoUri);
        void clearPhotoUri();
    }
}
