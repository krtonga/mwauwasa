package com.github.codetanzania.feature.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TextDrawable extends Drawable {

    private final String mText;
    private final Paint mPaint;

    public TextDrawable(@NonNull String text, Paint paint) {
        mText = text;
        if (paint == null) {
            mPaint = new Paint();
            mPaint.setTextSize(48f);
            mPaint.setTextAlign(Paint.Align.LEFT);
            mPaint.setColor(Color.BLACK);
            mPaint.setAntiAlias(true);
        } else {
            mPaint = paint;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(mText, 0, 16, mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
