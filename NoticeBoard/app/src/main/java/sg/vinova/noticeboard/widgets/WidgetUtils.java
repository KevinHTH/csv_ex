package sg.vinova.noticeboard.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mb on 3/17/16
 */
public class WidgetUtils {
    /**
     * Auto hide keyboard when touch outside input fields
     *
     * @param activity activity context
     * @param view     root view to receive touch listener
     */
    public static void setUpHideSoftKeyboard(final Activity activity, final View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) { //If a layout container, iterate over children and seed recursion.
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpHideSoftKeyboard(activity, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static boolean isTruncatedText(TextView textView) {
        int textHeight = textView.getLineCount() * textView.getLineHeight();
        return textHeight > textView.getHeight();
    }
}
