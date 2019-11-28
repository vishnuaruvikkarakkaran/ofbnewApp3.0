package com.enfin.ofabee3.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class AvenirTextView extends AppCompatTextView {

    public AvenirTextView(Context context) {
        super(context);
        init();
    }

    public AvenirTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirTextView(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Avenir-Medium.ttf");
            setTypeface(tf);
        }
    }

}