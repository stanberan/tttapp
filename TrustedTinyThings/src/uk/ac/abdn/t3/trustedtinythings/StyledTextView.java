package uk.ac.abdn.t3.trustedtinythings;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
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
    	if(!this.isInEditMode()) {
    		Typeface tf=null;
    		if(this.getTypeface()!=null && this.getTypeface().isBold()){
    			 tf = Typeface.createFromAsset(context.getAssets(),
     	                "fonts/Roboto-Bold.ttf");
    		}
    		else{
    		  // some code which will help the view instantiate
    		 tf = Typeface.createFromAsset(context.getAssets(),
    	                "fonts/Roboto-Light.ttf");
    	        
    		}
    		setTypeface(tf);
    		}
       
    }

}