package uk.ac.abdn.t3.trustedtinythings;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class StyledButton extends Button {

	 public StyledButton(Context context) {
	        super(context);
	        style(context);
	    }

	    public StyledButton(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        style(context);
	    }

	    public StyledButton(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        style(context);
	    }
	
	    private void style(Context context) {
	        Typeface tf = Typeface.createFromAsset(context.getAssets(),
	                "fonts/Roboto-Light.ttf");
	        setTypeface(tf);
	    }

}
