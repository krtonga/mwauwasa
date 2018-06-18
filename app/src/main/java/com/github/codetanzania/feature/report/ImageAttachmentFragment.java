package com.github.codetanzania.feature.report;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.codetanzania.open311.android.library.utils.AttachmentUtils;

import java.util.Locale;

import tz.co.codetanzania.R;

public class ImageAttachmentFragment extends Fragment {

    private static final String TAG = "ImageAttachment";

    /* instance to the picture item */
    private static final String ARG_PHOTO_URI = "image.path";

    /* the flag to fix rotation prevailing in some devices */
    private static final String ARG_FIX_ROTATION = "image.rotation";


    /* image view used to render captured image */
    private ImageView mPreviewItem;

    /* button to remove captured item */
    private ImageButton mBtnRemovePreviewItem;

    /* the reference will be initialized when the fragment is attached to the activity */
    private OnRemovePreviewItemClick mOnRemovePreviewItemClick;

    public static ImageAttachmentFragment getNewInstance(String data, boolean mayRotate) {
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URI, data);
        args.putBoolean(ARG_FIX_ROTATION, mayRotate);
        ImageAttachmentFragment frag = new ImageAttachmentFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override public void onAttach(Context ctx) {
        super.onAttach(ctx);
        try {
            mOnRemovePreviewItemClick = (OnRemovePreviewItemClick) ctx;
        } catch (ClassCastException cce) {
            throw new ClassCastException(String.format(Locale.getDefault(),
                    "%s must implement %s", getActivity().getClass().getName(),
                     OnRemovePreviewItemClick.class.getName()));
        }
    }

    /* invoked by android to create view for the fragment */
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup group,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_image_attachment, group, false);
        mPreviewItem = (ImageView) view.findViewById(R.id.img_AttachmentPreview);
        mBtnRemovePreviewItem = (ImageButton) view.findViewById(R.id.btn_RemoveItem);
        return view;
    }

    /* the callback to execute when the view is created */
    @Override public void onViewCreated(View fragView, Bundle savedInstanceState) {
        renderImage();
        handleEvents();
    }

    /* render image item */
    private void renderImage() {
        final String photoUri = getArguments().getString(ARG_PHOTO_URI);
        if (photoUri == null) {
            return;
        }

        mPreviewItem.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AttachmentUtils.setPicFromFile(mPreviewItem, photoUri);
            }
        });
    }

    /* attach events */
    private void handleEvents() {
        /* when the remove item is clicked */
        mBtnRemovePreviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRemovePreviewItemClick.onRemovePreviewItemClicked();
            }
        });
    }

    /* respond to "remove item" events */
    public interface OnRemovePreviewItemClick {
        void onRemovePreviewItemClicked();
    }
}
