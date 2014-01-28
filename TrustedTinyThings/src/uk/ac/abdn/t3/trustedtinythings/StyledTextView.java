package uk.ac.abdn.t3.trustedtinythings;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class StyledTextView extends TextView {

    public StyledTextView(Context context) {
        super(context);
        style(context);
    }

    public StyledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public StyledTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        setTypeface(tf);
    }

}