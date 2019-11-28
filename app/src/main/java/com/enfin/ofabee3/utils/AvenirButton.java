package com.enfin.ofabee3.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class AvenirButton extends AppCompatButton {

    public AvenirButton(Context context) {
        super(context);
        init();
    }

    public AvenirButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirButton(Context context, AttributeSet attrs,
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
