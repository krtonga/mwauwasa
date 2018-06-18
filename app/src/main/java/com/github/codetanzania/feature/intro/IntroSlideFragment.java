package com.github.codetanzania.feature.intro;

import android.os.Bundle;

import tz.co.codetanzania.R;

/**
 * This provides a custom template for intro slides.
 */

public class IntroSlideFragment extends AppIntroBaseFragment {

    public static IntroSlideFragment newInstance(String title, String description, int drawableResId, int backgroundColor, int titleColor, int descriptionColor) {
        IntroSlideFragment slide = new IntroSlideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TITLE_TYPEFACE, null);
        args.putString(ARG_DESC, description);
        args.putString(ARG_DESC_TYPEFACE, null);
        args.putInt(ARG_DRAWABLE, drawableResId);
        args.putInt(ARG_BG_COLOR, backgroundColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descriptionColor);
        slide.setArguments(args);

        return slide;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_intro_slide;
    }
}
