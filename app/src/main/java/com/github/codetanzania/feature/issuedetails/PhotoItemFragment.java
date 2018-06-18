package com.github.codetanzania.feature.issuedetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.github.codetanzania.open311.android.library.utils.AttachmentUtils;

import tz.co.codetanzania.R;

public class PhotoItemFragment extends Fragment {
    public static final String TAG =  "PhotoItemFragment";
    public static final String KEY_PHOTO_DATA = PhotoItemFragment.class.getSimpleName() + "/photo_data";

    private String mPhotoPath;

    public static PhotoItemFragment getNewInstance(@NonNull String imageUri) {
        Bundle args = new Bundle();
        args.putString(PhotoItemFragment.KEY_PHOTO_DATA, imageUri);
        PhotoItemFragment frag = new PhotoItemFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        // Uri is also parcelable [public abstract class Uri implements Parcelable, Comparable<Uri>]
        mPhotoPath = bundle.getString(KEY_PHOTO_DATA);
        // debug
        Log.d(TAG, String.format("%s", mPhotoPath));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_PHOTO_DATA, mPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.card_view_issue_image, viewGroup, false);
        final ImageView imgView = (ImageView) fragView.findViewById(R.id.imgView_Picture);
        imgView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AttachmentUtils.setPicFromFile(imgView, mPhotoPath);
            }
        });
        return fragView;
    }
}
