package com.enfin.ofabee3.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

public class AvenirEditText extends AppCompatEditText {

    private AvenirEditTextListener avenirEditTextListener;

    public AvenirEditText(Context context) {
        super(context);
        init();
    }

    public AvenirEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirEditText(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void addListener(AvenirEditTextListener listener) {
        this.avenirEditTextListener = listener;
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Avenir-Medium.ttf");
            setTypeface(tf);
        }
    }


    /**
     * <p>This is where the "magic" happens.</p>
     * <p>The menu used to cut/copy/paste is a normal ContextMenu, which allows us to
     *  overwrite the consuming method and react on the different events.</p>
     * @see <a href="http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3_r1/android/widget/TextView.java#TextView.onTextContextMenuItem%28int%29">Original Implementation</a>
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        // Do your thing:
        boolean consumed = super.onTextContextMenuItem(id);
        // React:
        switch (id){
            case android.R.id.cut:
                onTextCut();
                break;
            case android.R.id.paste:
                onTextPaste();
                break;
            case android.R.id.copy:
                onTextCopy();
        }
        return consumed;
    }

    /**
     * Text was cut from this EditText.
     */
    public void onTextCut(){
    }

    /**
     * Text was copied from this EditText.
     */
    public void onTextCopy(){
    }

    /**
     * Text was pasted into the EditText.
     */
    public void onTextPaste(){
        //avenirEditTextListener.onUpdate();
    }

    public interface AvenirEditTextListener {
        void onUpdate();
    }
}
